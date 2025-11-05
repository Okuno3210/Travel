
package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.TouristSpot;

public interface TouristSpotRepository extends JpaRepository<TouristSpot, Long> {
	 // Region に紐づく観光スポットを取得
    List<TouristSpot> findByRegionId(Long regionId);
}
