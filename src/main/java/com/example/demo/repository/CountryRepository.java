package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    // Optionalで返す
    Optional<Country> findByCode(String code);
}

