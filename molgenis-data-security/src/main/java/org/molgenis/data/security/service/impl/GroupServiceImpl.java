package org.molgenis.data.security.service.impl;

import com.google.common.collect.Range;
import org.molgenis.data.DataService;
import org.molgenis.data.MolgenisDataException;
import org.molgenis.data.meta.model.Package;
import org.molgenis.data.meta.model.PackageFactory;
import org.molgenis.data.meta.model.PackageMetadata;
import org.molgenis.data.security.model.*;
import org.molgenis.security.core.model.*;
import org.molgenis.security.core.service.GroupMembershipService;
import org.molgenis.security.core.service.GroupService;
import org.molgenis.security.core.service.RoleService;
import org.molgenis.security.core.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.google.common.collect.Lists.newArrayList;
import static java.time.Instant.now;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.molgenis.data.security.model.GroupMetadata.GROUP;
import static org.molgenis.security.core.model.Group.builder;

@Component
public class GroupServiceImpl implements GroupService
{
	private final GroupMembershipService groupMembershipService;
	private final DataService dataService;
	private final GroupFactory groupFactory;
	private final RoleFactory roleFactory;
	private final RoleService roleService;
	private final PackageFactory packageFactory;
	private final UserService userService;

	public GroupServiceImpl(GroupMembershipService groupMembershipService, DataService dataService,
			GroupFactory groupFactory, RoleFactory roleFactory, RoleService roleService, PackageFactory packageFactory,
			UserService userService)
	{
		this.groupMembershipService = requireNonNull(groupMembershipService);
		this.dataService = requireNonNull(dataService);
		this.groupFactory = requireNonNull(groupFactory);
		this.roleFactory = requireNonNull(roleFactory);
		this.roleService = requireNonNull(roleService);
		this.packageFactory = requireNonNull(packageFactory);
		this.userService = requireNonNull(userService);
	}

	@Override
	public Optional<Group> findGroupById(String groupId)
	{
		return Optional.ofNullable(dataService.findOneById(GROUP, groupId, GroupEntity.class))
					   .map(GroupEntity::toGroup);
	}

	@Override
	public void addGroupMembership(GroupMembership membership)
	{
		groupMembershipService.add(Collections.singletonList(membership));
	}

	private List<GroupMembership> getOverlapsSameUser(GroupMembership newMembership)
	{
		return groupMembershipService.getGroupMemberships(newMembership.getUser())
									 .stream()
									 .filter(newMembership::isOverlappingWith)
									 .collect(toList());
	}

	@Override
	@Transactional
	public void removeUserFromGroup(User user, Group group)
	{
		Instant end = now();
		List<GroupMembership> overlaps = getOverlapsSameUser(
				GroupMembership.builder().user(user).group(group).validity(Range.atLeast(end)).build());
		groupMembershipService.delete(overlaps);
		Optional<GroupMembership> currentMembership = overlaps.stream().filter(GroupMembership::isCurrent).findFirst();
		currentMembership.map(GroupMembership::toBuilder)
						 .map(builder -> builder.end(end))
						 .map(GroupMembership.Builder::build)
						 .map(Collections::singletonList)
						 .ifPresent(groupMembershipService::add);
	}

	@Override
	public List<GroupMembership> getGroupMemberships(User user)
	{
		return groupMembershipService.getGroupMemberships(user);
	}

	@Override
	public List<GroupMembership> getGroupMemberships(Group group)
	{
		return groupMembershipService.getGroupMemberships(group);
	}

	@Override
	public Set<Group> getCurrentGroups(User user)
	{
		return getGroupMemberships(user).stream()
										.filter(GroupMembership::isCurrent)
										.map(GroupMembership::getGroup)
										.collect(Collectors.toSet());
	}

	@Override
	public Group createGroup(String label, String ownerUserId)
	{
		// Todo move to/or use existing service for the naming
		// Only letters (a-z, A-Z), digits (0-9), underscores(_) and hashes (#) are allowed."
		String basePackageId = label.replaceAll("[^a-zA-Z0-9_#]+", "_").toLowerCase();

		Package groupPackage = packageFactory.create(basePackageId, label + " root package");
		groupPackage.setLabel(label);
		dataService.add(PackageMetadata.PACKAGE, groupPackage);

		GroupEntity groupRoot = groupFactory.create(label, groupPackage);
		dataService.add(GROUP, groupRoot);

		List<Role> roles = roleService.createRolesForGroup(groupRoot.getLabel());
		//Todo how do we know the 'admin' group ?, for now just use the label but this needs to change
		List<Group> groups = StreamSupport.stream(roles.spliterator(), false)
										   .map(role -> addChildGroups(groupRoot, role))
										   .collect(Collectors.toList());
		Optional<Group> adminGroup = groups.stream()
												  .filter(g -> g.getLabel()
																.endsWith(ConceptualRoles.GROUPADMIN.getDescription()))
												  .findFirst();
		if (!adminGroup.isPresent())
		{
			throw new MolgenisDataException("Could not create new group, no admin group found");
		}

		// Todo should this be part of group creating ?
		Optional<User> adminUser = userService.findUserById(ownerUserId);

		if (!adminUser.isPresent())
		{
			throw new MolgenisDataException("Could not create new group, no user found for given ownerUserId");
		}

		GroupMembership groupMembership = GroupMembership.builder()
														 .group(adminGroup.get())
														 .user(adminUser.get())
														 .start(now())
														 .build();
		groupMembershipService.add(Collections.singletonList(groupMembership));

		return groupRoot.toGroup().toBuilder().build();
	}

	private Group addChildGroups(GroupEntity parent, Role role)
	{
		Group childGroup = builder().label(role.getLabel())
									.groupPackageIdentifier(parent.getGroupPackage().getId())
									.roles(newArrayList(role))
									.build();
		GroupEntity childGroupEntity = groupFactory.create().updateFrom(childGroup, groupFactory, roleFactory);
		childGroupEntity.setParent(parent);
		dataService.add(GROUP, childGroupEntity);
		return childGroupEntity.toGroup();
	}

	@Override
	public void deleteGroup(String groupId)
	{
		GroupEntity rootGroup = dataService.findOneById(GroupMetadata.GROUP, groupId, GroupEntity.class);
		deleteGroupAndSubgroups(rootGroup);
		dataService.delete(PackageMetadata.PACKAGE, rootGroup.getGroupPackage());
	}

	private void deleteGroupAndSubgroups(GroupEntity group) {
		group.getChildren().forEach(this::deleteGroupAndSubgroups);
		List<GroupMembership> groupMemberships = groupMembershipService.getGroupMemberships(group.toGroup());
		groupMembershipService.delete(groupMemberships);
		Iterable<RoleEntity> groupRoles = group.getRoles();
		dataService.delete(GroupMetadata.GROUP, group);
		groupRoles.forEach(this::deleteRole);
	}

	private void deleteRole(RoleEntity role) {
		// Todo Find groups revering to this role and remove the role from is roles list
		dataService.delete(RoleMetadata.ROLE, role);
	}

	@Override
	public void removeRoleFromGroup(Group group, Role role)
	{
		updateGroup(group.toBuilder()
						 .roles(group.getRoles()
									 .stream()
									 .filter(sourceRole -> !sourceRole.getId().equals(role.getId()))
									 .collect(toList()))
						 .build());
	}

	@Override
	public void addRoleToGroup(Group group, Role role)
	{
		updateGroup(group.toBuilder().addRole(role).build());
	}

	private void updateGroup(Group group)
	{
		dataService.update(GROUP, groupFactory.create().updateFrom(group, groupFactory, roleFactory));
	}

}
