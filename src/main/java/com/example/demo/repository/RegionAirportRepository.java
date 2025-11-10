package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Airport;
import com.example.demo.entity.Region;
import com.example.demo.entity.RegionAirport;

public interface RegionAirportRepository extends JpaRepository<RegionAirport, Long> {
    List<RegionAirport> findByRegion(Region region);
    
    @Query("SELECT ra.airport FROM RegionAirport ra WHERE ra.region.id = :regionId")
    List<Airport> findAirportsByRegionId(@Param("regionId") Long regionId);

    @Query("SELECT ra.region FROM RegionAirport ra WHERE ra.airport.code = :code")
    List<Region> findRegionByAirportCode(@Param("code") String code);

}
