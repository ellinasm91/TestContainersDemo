package com.michalisellinas.testcontainersdemo.store;

import com.michalisellinas.testcontainersdemo.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepo extends JpaRepository<City, Long> {
    List<City> findByName(String name);

    Optional<City> findUniqueByName(String name);
}
