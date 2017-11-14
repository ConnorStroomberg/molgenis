package org.molgenis.groups.ui;

import org.molgenis.data.i18n.PropertiesMessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupsUiI18nConfig
{

	public static final String NAMESPACE = "groups-ui";

	@Bean
	public PropertiesMessageSource groupsUiMessageSource()
	{
		return new PropertiesMessageSource(NAMESPACE);
	}
}
