package com.crosby.recipe.it

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.testcontainers.containers.PostgreSQLContainer

import javax.sql.DataSource

@TestConfiguration
class IntegrationTestConfig {

    @Bean(initMethod="start",destroyMethod="stop")
    PostgreSQLContainer postgreSQLContainer() {
        return new PostgreSQLContainer("postgres:12.2")
    }

    @Bean(name = "datasource")
    DataSource dataSource(PostgreSQLContainer postgreSQLContainer) {
        DriverManagerDataSource sharedDataSource = new DriverManagerDataSource()
        sharedDataSource.setUrl(postgreSQLContainer.getJdbcUrl())
        sharedDataSource.setUsername(postgreSQLContainer.getUsername())
        sharedDataSource.setPassword(postgreSQLContainer.getDatabaseName())
        sharedDataSource.setDriverClassName(postgreSQLContainer.getDriverClassName())
        return sharedDataSource
    }
}
