package org.molgenis.oneclickimporter.model;

import org.molgenis.data.jobs.model.JobExecutionMetaData;
import org.molgenis.data.jobs.model.JobPackage;
import org.molgenis.data.meta.SystemEntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;
import static org.molgenis.data.jobs.model.JobPackage.PACKAGE_JOB;
import static org.molgenis.data.meta.model.Package.PACKAGE_SEPARATOR;

@Component
public class ImportJobExecutionMetadata extends SystemEntityType
{
	private static final String SIMPLE_NAME = "OneClickImportJobExecution";
	public static final String SCRIPT_JOB_EXECUTION = PACKAGE_JOB + PACKAGE_SEPARATOR + SIMPLE_NAME;

	public static final String FILE_NAME = "fileName";

	private final JobExecutionMetaData jobExecutionMetaData;
	private final JobPackage jobPackage;

	@Autowired
	ImportJobExecutionMetadata(JobExecutionMetaData jobExecutionMetaData, JobPackage jobPackage)
	{
		super(SIMPLE_NAME, PACKAGE_JOB);
		this.jobExecutionMetaData = requireNonNull(jobExecutionMetaData);
		this.jobPackage = requireNonNull(jobPackage);
	}

	@Override
	public void init()
	{
		setLabel("one click import job execution");
		setExtends(jobExecutionMetaData);
		setPackage(jobPackage);
		addAttribute(FILE_NAME).setLabel("File name").setDescription("Name of the file to import from").setNillable(false);
	}
}
