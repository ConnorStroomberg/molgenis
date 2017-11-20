package org.molgenis.data.security.model;

import org.molgenis.data.AbstractSystemEntityFactory;
import org.molgenis.data.meta.model.Package;
import org.molgenis.data.populate.EntityPopulator;
import org.molgenis.data.populate.IdGenerator;
import org.springframework.stereotype.Component;

@Component
public class GroupFactory extends AbstractSystemEntityFactory<GroupEntity, GroupMetadata, String>
{
	GroupFactory(GroupMetadata groupMetaData, EntityPopulator entityPopulator)
	{
		super(GroupEntity.class, groupMetaData, entityPopulator);
	}

	public GroupEntity create(String label, Package groupPackage)
	{
		GroupEntity groupEntity = create();
		groupEntity.setLabel(label);
		groupEntity.setGroupPackage(groupPackage);
		return groupEntity;
	}

	public GroupEntity create(String label, GroupEntity parent)
	{
		GroupEntity groupEntity = create();
		groupEntity.setLabel(label);
		groupEntity.setParent(parent);
		return groupEntity;
	}
}
