package com.michalisellinas.testcontainersdemo.cache;

import com.michalisellinas.testcontainersdemo.model.City;

import java.util.Map;
import java.util.Optional;

public class HashMapCache implements Cache<City> {
  private Map<String, City> cache;

  public HashMapCache(Map<String, City> cache) {
    this.cache = cache;
  }

  @Override
  public void put(String key, City value) {
    cache.put(key, value);
  }

  @Override
  public Optional<City> get(String key) {

    return Optional.ofNullable(cache.get(key));
  }
}
