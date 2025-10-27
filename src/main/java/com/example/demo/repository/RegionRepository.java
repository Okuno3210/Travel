
package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Region;

public interface RegionRepository extends JpaRepository
<Region, Long> {

	@Query("""
		    SELECT DISTINCT r
		    FROM Region r
		    LEFT JOIN FETCH r.country c
		    LEFT JOIN FETCH r.touristSpots ts
		    WHERE (:regionId IS NULL OR r.id = :regionId)
		    AND(:countryName IS NULL OR c.name =:countryName)
		    AND(:regionName IS NULL OR r.name =:regionName)
		      AND (:spotId IS NULL OR ts.id =:spotId)
		      AND (:travelTime IS NULL OR r.flightTime <= :travelTime)
		      AND (:budget IS NULL OR r.budget <= :budget)
		    """)
		List<Region> searchRegions(
				@Param("countryName") String countryName,
		        @Param("regionId") Long regionId,
		        @Param("regionName") String regionName, //地域名
		        @Param("spotId") String spotId, //ツーリストスポット名
		        @Param("travelTime") Integer travelTime,
		        @Param("budget") Integer budget);

    // ★ CSV追加時はLEFT JOIN + AND 条件を追記
	@Query("SELECT DISTINCT r FROM Region r LEFT "
			+ "JOIN FETCH r.touristSpots LEFT "
			+ "JOIN FETCH r.country c")
	List<Region>findAllWithSpots();
}
