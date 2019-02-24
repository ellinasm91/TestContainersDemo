package com.michalisellinas.testcontainersdemo.service;

import com.michalisellinas.testcontainersdemo.cache.Cache;
import com.michalisellinas.testcontainersdemo.model.City;
import com.michalisellinas.testcontainersdemo.store.CityRepo;

import java.util.Optional;

class CityService {
  private Cache<City> cache;
  private CityRepo repo;

  CityService(Cache<City> cache, CityRepo repo) {
    this.cache = cache;
    this.repo = repo;
  }

  void put(String key, City value) {}

  Optional<City> get(String key) {
    Optional<City> cachedValue = cache.get(key);
    if (cachedValue.isPresent()) {
      return cachedValue;
    } else {
      Optional<City> repoData = repo.findUniqueByName(key);
      repoData.ifPresent(city -> cache.put(key, city));
      return repoData;
    }
  }
}
