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
    
    //↓ツアー一覧から空港検索画面遷移用
    List<Airport> findByCountryId(Long countryId);



}
