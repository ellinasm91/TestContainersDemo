package com.michalisellinas.testcontainersdemo.store;

import com.michalisellinas.testcontainersdemo.model.City;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CitiesWithPostgresContainerTest extends PostgresContainerBaseTestClass {
  private static final Logger log = LoggerFactory.getLogger(CitiesWithPostgresContainerTest.class);

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
}
