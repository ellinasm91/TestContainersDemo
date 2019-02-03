package com.michalisellinas.testcontainersdemo.store;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Duration;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(initializers = {PostgresContainerBaseTestClass.Initializer.class})
public class PostgresContainerBaseTestClass {
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer =
            (PostgreSQLContainer)
                    new PostgreSQLContainer("postgres:10.4")
                            .withDatabaseName("sampledb")
                            .withUsername("sampleuser")
                            .withPassword("samplepwd")
                            .withStartupTimeout(Duration.ofSeconds(600));


    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword())
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
