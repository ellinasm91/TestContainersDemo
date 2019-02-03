package com.michalisellinas.testcontainersdemo.cache;

import com.michalisellinas.testcontainersdemo.model.City;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class HashMapCacheTest {
  private static final Logger log = LoggerFactory.getLogger(HashMapCacheTest.class);

  private static City CITY1 = new City(null, "city1", "USA", 20000L);
  private Cache<City> cache;
  private HashMap<String, City> map;

  @Before
  public void setUp() {
    map = new HashMap<>();
    cache = new HashMapCache(map);
  }

  @Test
  public void put() {
    cache.put(CITY1.getName(), CITY1);

    City result = map.getOrDefault(CITY1.getName(), null);

    log.debug("Result: {}", result);
    assertThat(result).isEqualTo(CITY1);
  }

  @Test
  public void get() {
    map.put(CITY1.getName(), CITY1);

    Optional<City> result = cache.get(CITY1.getName());

    log.debug("Result: {}", result.orElseGet(null));
    assertThat(result).containsSame(CITY1);
  }
}
