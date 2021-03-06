package com.michalisellinas.testcontainersdemo.store;

import com.michalisellinas.testcontainersdemo.model.City;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(initializers = {CitiesWithPostgresContainerTest.Initializer.class})
public class CitiesWithPostgresContainerTest {
  private static final Logger log = LoggerFactory.getLogger(CitiesWithPostgresContainerTest.class);
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer =
            (PostgreSQLContainer)
                    new PostgreSQLContainer("postgres:10.4")
                            .withDatabaseName("sampledb")
                            .withUsername("sampleuser")
                            .withPassword("samplepwd")
                            .withStartupTimeout(Duration.ofSeconds(600));
  @Autowired private CityRepo cityRepo;

  @Test
  public void testWithDb() {
    City city1 = cityRepo.save(new City(null, "city1", "USA", 20000L));
    City city2 = cityRepo.save(new City(null, "city2", "USA", 40000L));
    assertThat(city1)
        .matches(
            c ->
                c.getId() != null && "city1".equalsIgnoreCase(c.getName()) && c.getPop() == 20000L);
    assertThat(city2)
        .matches(
            c ->
                c.getId() != null && "city2".equalsIgnoreCase(c.getName()) && c.getPop() == 40000L);
    assertThat(cityRepo.findAll()).containsExactly(city1, city2);
  }

  @Test
  public void testFindByName() {
    City city1 = cityRepo.save(new City(null, "city1", "USA", 20000L));
    City city2 = cityRepo.save(new City(null, "city2", "USA", 40000L));

    List<City> result = cityRepo.findByName("city2");

    log.warn("Result city: {}", result);
    assertThat(result).containsExactly(city2);
  }

    @Test
    public void testFindUniqueByName() {
        City city1 = cityRepo.save(new City(null, "city1", "USA", 20000L));
        City city2 = cityRepo.save(new City(null, "city2", "USA", 40000L));

        Optional<City> result = cityRepo.findUniqueByName("city2");

        log.warn("Result city: {}", result);
        assertThat(result).containsSame(city2);
    }

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
