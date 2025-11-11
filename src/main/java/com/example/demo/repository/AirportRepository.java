package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Airport;
import com.example.demo.entity.Country;
import com.example.demo.entity.Region;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    List<Airport> findByCountry(Country country);
    
    Optional<Airport> findByCode(String code);
    
    List<Airport> findByRegion(Region region);


}
