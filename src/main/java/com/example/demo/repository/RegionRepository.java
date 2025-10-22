
package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {

	@Query("""
		    SELECT DISTINCT r
		    FROM Region r
		    LEFT JOIN r.country c
		    LEFT JOIN TouristSpot ts ON ts.regionId = r.id
		    WHERE (:regionId IS NULL OR r.id = :regionId)
		      AND (:spotName IS NULL OR ts.name LIKE %:spotName%)
		      AND (:travelTime IS NULL OR r.flightTime <= :travelTime)
		      AND (:budget IS NULL OR r.budget <= :budget)
		    """)
		List<Region> searchRegions(
		        @Param("regionId") Long regionId,
		        @Param("spotName") String spotName,
		        @Param("travelTime") Integer travelTime,
		        @Param("budget") Integer budget);


    // ★ CSV追加時はLEFT JOIN + AND 条件を追記
}
