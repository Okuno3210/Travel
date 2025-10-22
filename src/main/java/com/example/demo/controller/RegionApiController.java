package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Region;
import com.example.demo.entity.TouristSpot;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.TouristSpotRepository;

@RestController
@RequestMapping("/api/regions")
public class RegionApiController {

    private final RegionRepository regionRepo;
    private final TouristSpotRepository spotRepo;

    public RegionApiController(RegionRepository regionRepo, TouristSpotRepository spotRepo) {
        this.regionRepo = regionRepo;
        this.spotRepo = spotRepo;
    }

    // ▼ region の選択肢取得（CSVから読み込んだデータを返す）
    @GetMapping("/options")
    public List<Map<String, Object>> getRegionOptions() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Region r : regionRepo.findAll()) {
            result.add(Map.of("id", r.getId(), "name", r.getName()));
        }
        return result;
    }

    // ▼ 検索機能（region・spot・時間・予算を条件に検索）
    @GetMapping("/search")
    public List<Map<String, Object>> searchRegions(
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) String spotName,
            @RequestParam(required = false) Integer travelTime,
            @RequestParam(required = false) Integer budget
    ) {
        return regionRepo.findAll().stream()
                .filter(r -> regionId == null || r.getId().equals(regionId))
                .filter(r -> travelTime == null || r.getFlightTime() <= travelTime)
                .filter(r -> budget == null || r.getBudget() <= budget)
                .map(r -> {
                    List<String> spots = spotRepo.findAll().stream()
                            .filter(s -> s.getRegionId().equals(r.getId()))
                            .filter(s -> spotName == null || s.getName().contains(spotName))
                            .map(TouristSpot::getName)
                            .collect(Collectors.toList());
                    return Map.of(
                            "id", r.getId(),
                            "name", r.getName(),
                            "budget", r.getBudget(),
                            "flightTime", r.getFlightTime(),
                            "spots", spots
                    );
                })
                .filter(r -> !((List<?>) r.get("spots")).isEmpty())
                .collect(Collectors.toList());
    }
}
