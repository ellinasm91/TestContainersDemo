package com.michalisellinas.testcontainersdemo.service;

import com.michalisellinas.testcontainersdemo.cache.Cache;
import com.michalisellinas.testcontainersdemo.cache.HashMapCache;
import com.michalisellinas.testcontainersdemo.model.City;
import com.michalisellinas.testcontainersdemo.store.CityRepo;
import org.assertj.core.api.Assertions;
import org.junit.Before;
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
import java.util.HashMap;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(initializers = {CityServiceTest.Initializer.class})
public class CityServiceTest {
    private static final Logger log = LoggerFactory.getLogger(CityServiceTest.class);

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer =
            (PostgreSQLContainer)
                    new PostgreSQLContainer("postgres:10.4")
                            .withDatabaseName("sampledb")
                            .withUsername("sampleuser")
                            .withPassword("samplepwd")
                            .withStartupTimeout(Duration.ofSeconds(600));

    private HashMap<String, City> cacheMap;
    @Autowired
    private CityRepo cityRepo;
    private Cache cache;
    private CityService service;

    @Before
    public void setUp() {
        cacheMap = new HashMap<>();
        this.cache = new HashMapCache(cacheMap);
        service = new CityService(this.cache, cityRepo);
    }

    @Test
    public void whenGetttingValueFetchFromStoreAndWriteToCache() {
        City storedCity = cityRepo.save(new City(null, "city1", "USA", 20000L));

        Optional<City> resultFromService = service.get(storedCity.getName());
        City resourceInCache = cacheMap.get(storedCity.getName());

        Assertions.assertThat(resultFromService).contains(storedCity);
        Assertions.assertThat(resourceInCache).isEqualTo(storedCity);
    }

    @Test
    public void whenValueNotPresentInStoreSkipCacheWriteAndReturnEmpty() {
        City city = new City(null, "city1", "USA", 20000L);

        Optional<City> resultFromService = service.get(city.getName());
        City resourceInCache = cacheMap.get(city.getName());

        Assertions.assertThat(resultFromService).isEmpty();
        Assertions.assertThat(resourceInCache).isNull();
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
