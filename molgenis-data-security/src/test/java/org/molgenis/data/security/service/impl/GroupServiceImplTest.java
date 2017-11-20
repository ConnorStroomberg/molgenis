package org.molgenis.data.security.service.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoSession;
import org.molgenis.data.DataService;
import org.molgenis.data.meta.model.Package;
import org.molgenis.data.meta.model.PackageFactory;
import org.molgenis.data.meta.model.PackageMetadata;
import org.molgenis.data.security.model.*;
import org.molgenis.security.core.model.*;
import org.molgenis.security.core.service.GroupMembershipService;
import org.molgenis.security.core.service.RoleService;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.time.Month.JANUARY;
import static java.time.ZoneOffset.UTC;
import static java.util.Collections.singleton;
import static org.mockito.Mockito.*;
import static org.mockito.quality.Strictness.STRICT_STUBS;
import static org.testng.Assert.assertEquals;

public class GroupServiceImplTest
{
	@Mock
	private User user;
	@Mock
	private GroupMembership groupMembership1;
	@Mock
	private GroupMembership groupMembership2;
	@Mock
	private GroupMembership groupMembership3;
	@Mock
	private Group group1;
	@Mock
	private Group group2;
	@Mock
	private Group group3;
	@Mock
	private Group group4;
	@Mock
	private GroupMembershipService groupMembershipService;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private GroupFactory groupFactory;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private RoleFactory roleFactory;
	@Mock
	private DataService dataService;
	@Mock
	private RoleService roleService;
	@Mock
	private PackageFactory packageFactory;

	@InjectMocks
	private GroupServiceImpl groupService;

	private MockitoSession mockitoSession;

	@BeforeMethod
	public void beforeMethod()
	{
		groupService = null;
		mockitoSession = mockitoSession().strictness(STRICT_STUBS).initMocks(this).startMocking();
	}

	@AfterMethod
	public void afterMethod()
	{
		mockitoSession.finishMocking();
	}

	//	@Test
	//	public void testAddGroupMembershipJoiningOverlaps()
	//	{
	//		/*
	//			Going to add user to group1.
	//			user already is a member of group2, group3, and group4
	//			           [------group1-----)
	//			[---group1---)  [group1)  [---group1---------
	//
	//			Desired outcome:
	//			[-----------------group1---------------------
	//		 */
	//		GroupMembership m1 = createMembership(null, 2, 5, group1);
	//		GroupMembership m2 = createMembership("m2", 1, 3, group1);
	//		GroupMembership m3 = createMembership("m3", 3, 4, group1);
	//		GroupMembership m4 = createMembership("m4", 4, null, group1);
	//		when(group1.hasSameParentAs(any(Group.class))).thenReturn(true);
	//		when(groupMembershipService.getGroupMemberships(user)).thenReturn(ImmutableList.of(m2, m3, m4));
	//
	//		groupService.addUserToGroup(m1.getUser(), m1.getGroup(), m1.getStart(), m1.getEnd().get());
	//
	//		GroupMembership allCombined = createMembership(null, 1, null, group1);
	//		verify(groupMembershipService).delete(ImmutableList.of(m2, m3, m4));
	//		verify(groupMembershipService).add(ImmutableList.of());
	//		verify(groupMembershipService).add(ImmutableList.of(allCombined));
	//	}
	//
	//	@Test
	//	public void testAddGroupMembershipConflictingOverlaps()
	//	{
	//		/*
	//			Going to add user to group1.
	//			user already is a member of group2, group3, and group4
	//			           [------group1-----)
	//			[---group2---)[--group3--)[---group4---------
	//
	//			Desired outcome:
	//			[---group2)[------group1-----)[-group4-------
	//		 */
	//		GroupMembership m1 = createMembership(null, 2, 5, group1);
	//		GroupMembership m2 = createMembership("m2", 1, 3, group2);
	//		GroupMembership m3 = createMembership("m3", 3, 4, group3);
	//		GroupMembership m4 = createMembership("m4", 4, null, group4);
	//		when(group1.hasSameParentAs(any(Group.class))).thenReturn(true);
	//		when(groupMembershipService.getGroupMemberships(user)).thenReturn(ImmutableList.of(m2, m3, m4));
	//
	//		groupService.addUserToGroup(m1.getUser(), m1.getGroup(), m1.getStart(), m1.getEnd().get());
	//
	//		GroupMembership m2Truncated = createMembership(null, 1, 2, group2);
	//		GroupMembership m4Truncated = createMembership(null, 5, null, group4);
	//		verify(groupMembershipService).delete(ImmutableList.of(m2, m3, m4));
	//		verify(groupMembershipService).add(ImmutableList.of(m2Truncated, m4Truncated));
	//		verify(groupMembershipService).add(ImmutableList.of(m1));
	//	}

	private GroupMembership createMembership(String id, int start, Integer end, Group group)
	{
		GroupMembership.Builder result = GroupMembership.builder().user(user).group(group).start(january(start));
		Optional.ofNullable(id).ifPresent(result::id);
		Optional.ofNullable(end).map(this::january).ifPresent(result::end);
		return result.build();
	}

	private Instant january(int day)
	{
		return LocalDate.of(2017, JANUARY, day).atStartOfDay(UTC).toInstant();
	}

	@Test
	public void testGetCurrentGroups()
	{
		when(groupMembershipService.getGroupMemberships(user)).thenReturn(
				ImmutableList.of(groupMembership1, groupMembership2, groupMembership3));

		when(groupMembership1.isCurrent()).thenReturn(true);
		when(groupMembership2.isCurrent()).thenReturn(false);
		when(groupMembership3.isCurrent()).thenReturn(true);

		when(groupMembership1.getGroup()).thenReturn(group1);
		when(groupMembership3.getGroup()).thenReturn(group1);

		assertEquals(groupService.getCurrentGroups(user), singleton(group1));
	}

	@Test
	public void testGetGroupMembershipsForGroup()
	{
		when(groupMembershipService.getGroupMemberships(group1)).thenReturn(
				ImmutableList.of(groupMembership1, groupMembership2, groupMembership3));

		assertEquals(groupService.getGroupMemberships(group1),
				ImmutableList.of(groupMembership1, groupMembership2, groupMembership3));
	}

	@Test
	public void testGetGroupMembershipsForUser()
	{
		when(groupMembershipService.getGroupMemberships(user)).thenReturn(
				ImmutableList.of(groupMembership1, groupMembership2, groupMembership3));

		assertEquals(groupService.getGroupMemberships(user),
				ImmutableList.of(groupMembership1, groupMembership2, groupMembership3));
	}

	@Test
	public void testCreateGroup()
	{
		String label = "BBMRI_NL";
		Package groupPackage = mock(Package.class);
		when(packageFactory.create(label.toLowerCase(), label + " root package")).thenReturn(groupPackage);
		GroupEntity groupRoot = mock(GroupEntity.class);
		when(groupRoot.getLabel()).thenReturn(label);
		Group group = Group.builder()
						   .id(label.toLowerCase())
						   .label(label)
						   .groupPackageIdentifier(label.toLowerCase())
						   .build();
		when(groupRoot.toGroup()).thenReturn(group);
		when(groupFactory.create(label, groupPackage)).thenReturn(groupRoot);
		Role role = Role.builder().id("roleId").label("roleLabel").build();
		List<Role> roles = Arrays.asList(role);
		when(roleService.createRolesForGroup(label)).thenReturn(roles);

		Group createdGroup = groupService.createGroup(label);

		assertEquals(createdGroup.getLabel(), label);
		verify(dataService).add(PackageMetadata.PACKAGE, groupPackage);
		verify(dataService).add(GroupMetadata.GROUP, groupRoot);
	}

	@Test
	public void testDeleteGroup()
	{
		String groupId = "groupsId-123";

		GroupEntity rootGroup = mock(GroupEntity.class);
		GroupEntity childGroupA = mock(GroupEntity.class);
		GroupEntity childGroupB = mock(GroupEntity.class);
		GroupEntity grandChildGroupA = mock(GroupEntity.class);
		RoleEntity role1 = mock(RoleEntity.class);
		RoleEntity role2 = mock(RoleEntity.class);
		RoleEntity role3 = mock(RoleEntity.class);
		when(dataService.findOneById(GroupMetadata.GROUP, groupId, GroupEntity.class)).thenReturn(rootGroup);
		Iterable<GroupEntity> childGroups = Arrays.asList(childGroupA, childGroupB);
		when(rootGroup.getChildren()).thenReturn(childGroups);
		when(childGroupA.getChildren()).thenReturn(Collections.singletonList(grandChildGroupA));
		Iterable<RoleEntity> roles = Arrays.asList(role1);
		when(rootGroup.getRoles()).thenReturn(roles);
		Iterable<RoleEntity> grandChildRoles = Arrays.asList(role2, role3);
		when(grandChildGroupA.getRoles()).thenReturn(grandChildRoles);
		Package groupPackage = mock(Package.class);
		when(rootGroup.getGroupPackage()).thenReturn(groupPackage);

		groupService.deleteGroup(groupId);

		verify(dataService).delete(RoleMetadata.ROLE, role2);
		verify(dataService).delete(RoleMetadata.ROLE, role3);
		verify(dataService).delete(RoleMetadata.ROLE, role1);

		verify(dataService).delete(GroupMetadata.GROUP, grandChildGroupA);
		verify(dataService).delete(GroupMetadata.GROUP, childGroupA);
		verify(dataService).delete(GroupMetadata.GROUP, childGroupB);
		verify(dataService).delete(GroupMetadata.GROUP, rootGroup);
		verify(dataService).delete(PackageMetadata.PACKAGE, groupPackage);
	}

}