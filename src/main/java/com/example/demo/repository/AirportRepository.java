package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Airport;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    List<Airport> findByCountry(String country);
    Optional<Airport> findByCode(String code);
    
 //↓ツアー一覧から空港検索画面遷移用
    List<Airport> findByCodeIn(List<String> codes);

}
