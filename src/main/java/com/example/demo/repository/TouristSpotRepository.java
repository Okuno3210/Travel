
package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.TouristSpot;

public interface TouristSpotRepository extends JpaRepository<TouristSpot, Long> {
	 // Region に紐づく観光スポットを取得
	@Query("SELECT s FROM TouristSpot s JOIN FETCH s.region WHERE s.region.id = :regionId")
    List<TouristSpot> findByRegionId(Long regionId);
}
