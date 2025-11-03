
package com.example.demo.repository; //10/30 concept追加

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {

	@Query("SELECT DISTINCT r FROM Region r "
			+ "LEFT JOIN FETCH r.country c "
			+ "LEFT JOIN FETCH r.touristSpots ts "
			+ "WHERE (:regionId IS NULL OR r.id = :regionId) "
			+ "AND (:countryId IS NULL OR c.id = :countryId) "
			+ "AND (:regionName IS NULL OR r.name = :regionName) "
			+ "AND (:touristSpotId IS NULL OR ts.id = :touristSpotId) "
			+ "AND (:flightTime IS NULL OR r.flightTime <= :flightTime) "
			+ "AND (:budget IS NULL OR r.budget <= :budget)")

	List<Region> searchRegions(
			@Param("countryId") Long countryId,
			@Param("regionName") String regionName, //地域名
			@Param("regionId") Long regionId,
			@Param("touristSpotId") String touristSpotId,
			@Param("flightTime") String flightTime, //10/30　Stringに変更
			@Param("budget") Integer budget,
			@Param("concept") String concept);

	// ★ CSV追加時はLEFT JOIN + AND 条件を追記
	@Query("SELECT DISTINCT r FROM Region r LEFT "
			+ "JOIN FETCH r.touristSpots LEFT "
			+ "JOIN FETCH r.country c")
	List<Region> findAllWithSpots();
}
