package org.molgenis.data.security.model;

import org.molgenis.data.AbstractMolgenisSpringTest;
import org.molgenis.data.MolgenisDataException;
import org.molgenis.data.meta.model.Package;
import org.molgenis.data.populate.EntityPopulator;
import org.molgenis.security.core.model.Group;
import org.molgenis.security.core.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@ContextConfiguration(classes = {GroupFactory.class, GroupMetadata.class , EntityPopulator.class, SecurityPackage.class,
RoleMetadata.class, RoleFactory.class})
public class GroupEntityTest extends AbstractMolgenisSpringTest
{
	private static final String EXPECTED_ERROR_MESSAGE = "Invalid group structure, non top level group must have a parent";

	@Autowired
	private GroupFactory groupFactory;
	@Autowired
	private RoleFactory roleFactory;

	@Test(expectedExceptions = MolgenisDataException.class, expectedExceptionsMessageRegExp = EXPECTED_ERROR_MESSAGE)
	public void getGroupPackageFromInValidGroupStructure()
	{
		String groupId = "groupId";
		GroupEntity group = groupFactory.create(groupId);
		group.getGroupPackage();
	}

	@Test
	public void getGroupPackageFromRootGroup()
	{
		Package rootPackage = mock(Package.class);
		GroupEntity rootGroup = groupFactory.create("label", rootPackage);
		assertEquals(rootGroup.getGroupPackage(), rootPackage);
	}

	@Test
	public void getGroupPackageFromChildGroup()
	{
		Package rootPackage = mock(Package.class);
		GroupEntity parentGroup = groupFactory.create("parent", rootPackage);
		GroupEntity rootGroup = groupFactory.create("label", parentGroup);

		assertEquals(rootGroup.getGroupPackage(), rootPackage);
	}

	@Test
	public void toGroup()
	{
		Group parentGroup = Group.builder().id("parent").label("parent label").groupPackageIdentifier("groupPackageId")
								 .build();
		List<Role> roles = Collections.singletonList(Role.builder().id("roleId").label("roleLabel").build());
		Group group = Group.builder().id("id").label("label").groupPackageIdentifier("groupPackageId")
						   .parent(parentGroup).roles(roles).build();
		Package groupPackage = mock(Package.class);
		when(groupPackage.getId()).thenReturn("groupPackageId");
		GroupEntity groupEntity = groupFactory.create("id");
		groupEntity.setLabel("label");

		RoleEntity role = roleFactory.create("roleId");
		role.setLabel("roleLabel");
		Iterable<RoleEntity> roleEntities = Collections.singletonList(role);
		groupEntity.setRoles(roleEntities);
		GroupEntity parentEntity = groupFactory.create("parent");
		parentEntity.setLabel("parent label");
		parentEntity.setGroupPackage(groupPackage);
		groupEntity.setParent(parentEntity);

		assertEquals(group, groupEntity.toGroup());
	}
}
