package org.molgenis.integrationtest.config;

import org.molgenis.data.postgresql.PostgreSqlConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * This class still uses {@link ComponentScan} because of class visibility in postgresql package.
 */
@Configuration
@Import(PostgreSqlConfiguration.class)
@ComponentScan("org.molgenis.data.postgresql")
public class PostgreSqlTestConfig {}
