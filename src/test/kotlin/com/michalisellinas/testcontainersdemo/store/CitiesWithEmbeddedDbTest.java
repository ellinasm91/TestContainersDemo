package com.michalisellinas.testcontainersdemo.store;

import com.michalisellinas.testcontainersdemo.model.City;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CitiesWithEmbeddedDbTest {
  @Autowired private CityRepo cityRepo;
  private static City CITY1 = new City(null, "city1", "USA", 20000L);
  private static City CITY2 = new City(null, "city2", "USA", 40000L);

  @Test
  public void testWithDb() {
    City city1 = cityRepo.save(CITY1);
    City city2 = cityRepo.save(CITY2);
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
}
