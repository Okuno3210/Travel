
package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TouristSpot;
@Repository
public interface TouristSpotRepository extends JpaRepository
<TouristSpot, String> {}
