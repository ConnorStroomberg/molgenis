package org.molgenis.metadata.manager.config;

import org.molgenis.data.jobs.Job;
import org.molgenis.data.jobs.JobFactory;
import org.molgenis.data.meta.model.EntityType;
import org.molgenis.oneclickimporter.model.ImportJobExecution;
import org.molgenis.oneclickimporter.service.Impl.ImportJobServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// FIXME org.molgenis.data.MolgenisDataException: ; nested exception is org.springframework.jdbc.BadSqlGrammarException: PreparedStatementCallback; bad SQL grammar [SELECT this."id", this."msgid", this."namespace", this."description", this."en", this."nl", this."de", this."es", this."it", this."pt", this."fr", this."xx" FROM "sys_L10nString#95a21e09" AS this WHERE this."msgid" IN () AND this."namespace" = ?  LIMIT 1000];
 @Configuration
public class OneClickImporterConfig
{
//	@Bean
//	public PropertiesMessageSource oneClickImporterMessageSource()
//	{
//		return new PropertiesMessageSource("one-click-importer");
//	}

	@Autowired
	ImportJobServiceImpl importJobService;

	@Bean
	public JobFactory<ImportJobExecution> helloWorldJobFactory()
	{
		return new JobFactory<ImportJobExecution>()
		{
			@Override
			public Job<EntityType> createJob(ImportJobExecution jobExecution)
			{
				final String fileName = jobExecution.getFileName();
				return progress -> importJobService.getEntityType(progress, fileName);
			}
		};
	}
}
