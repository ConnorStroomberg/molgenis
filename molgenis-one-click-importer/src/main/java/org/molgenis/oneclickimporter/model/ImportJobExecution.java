package org.molgenis.oneclickimporter.model;

import org.molgenis.data.Entity;
import org.molgenis.data.jobs.model.JobExecution;
import org.molgenis.data.meta.model.EntityType;

import static org.molgenis.oneclickimporter.model.ImportJobExecutionMetadata.FILE_NAME;

public class ImportJobExecution extends JobExecution
{
	public ImportJobExecution(Entity entity)
	{
		super(entity);
		setType("One click import");
	}

	public ImportJobExecution(EntityType entityType)
	{
		super(entityType);
		setType("One click import");
	}

	public ImportJobExecution(String identifier, EntityType entityType)
	{
		super(identifier, entityType);
		setType("One click import");
	}

	public void setFileName(String name)
	{
		set(FILE_NAME, name);
	}

	public String getFileName()
	{
		return getString(FILE_NAME);
	}


}
