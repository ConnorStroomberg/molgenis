package org.molgenis.security.group;

import com.google.auto.value.AutoValue;
import org.molgenis.gson.AutoGson;

import javax.annotation.Nullable;

@AutoValue
@AutoGson(autoValueClass = AutoValue_CreateGroup.class)
public abstract class CreateGroup
{
	public abstract String getLabel();

	@Nullable
	public abstract String getDescription();

	public abstract String getGroupOwnerId();

	public static CreateGroup create(String label, String desc, String groupOwnerId)
	{
		return new AutoValue_CreateGroup(label, desc, groupOwnerId);
	}

}
