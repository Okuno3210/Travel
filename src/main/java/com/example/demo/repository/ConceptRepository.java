package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Concept;

public interface ConceptRepository extends JpaRepository
<Concept, Long> {}
