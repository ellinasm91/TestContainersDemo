package com.michalisellinas.testcontainersdemo.service;

import com.michalisellinas.testcontainersdemo.cache.Cache;
import com.michalisellinas.testcontainersdemo.model.City;
import com.michalisellinas.testcontainersdemo.store.CityRepo;

import java.util.Optional;

public class CityService {
  private Cache cache;
  private CityRepo repo;

  public CityService(Cache cache, CityRepo repo) {
    this.cache = cache;
    this.repo = repo;
  }

  void put(String key, City value) {}

  Optional<City> get(String key) {
    return Optional.empty();
  }
}
