package com.example.demo.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Food;
import com.example.demo.entity.Region;
import com.example.demo.entity.TouristSpot;
import com.example.demo.repository.FoodRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.TouristSpotRepository;

@Service
@Transactional(readOnly = true)
public class TourismService {

    private final RegionRepository regionRepo;
    private final TouristSpotRepository spotRepo;
    private final FoodRepository foodRepo;

    public TourismService(RegionRepository regionRepo,
                          TouristSpotRepository spotRepo,
                          FoodRepository foodRepo) {
        this.regionRepo = regionRepo;
        this.spotRepo = spotRepo;
        this.foodRepo = foodRepo;
    }
    
    @Cacheable("region")
    public Region getRegion(Long regionId) {
        return regionRepo.findById(regionId).orElse(null);
    }
    
    @Cacheable("spots")
    public List<TouristSpot> getSpotsByRegion(Long regionId) {
        return spotRepo.findByRegionId(regionId);
    }
    
    @Cacheable("foods")
    public List<Food> getFoodsByRegion(Long regionId) {
        return foodRepo.findByRegionId(regionId);
    }
}
