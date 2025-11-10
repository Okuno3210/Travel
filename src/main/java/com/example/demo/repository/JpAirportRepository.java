package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.JpAirport;

public interface JpAirportRepository extends JpaRepository<JpAirport, Long> {
	
	Optional<JpAirport> findByCode(String code);
}
