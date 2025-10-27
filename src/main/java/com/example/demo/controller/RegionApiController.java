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

    private final CountryController countryController;
    private final RegionRepository regionRepo;
    private final TouristSpotRepository spotRepo;

    public RegionApiController(RegionRepository regionRepo, 
    TouristSpotRepository spotRepo, CountryController countryController) {
        this.regionRepo = regionRepo;
        this.spotRepo = spotRepo;
        this.countryController = countryController;
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
    		@RequestParam(required=false) String countryName,
    		@RequestParam(required=false)String regionName,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) String spotId,
            @RequestParam(required = false) Integer travelTime,
            @RequestParam(required = false) Integer budget
    ) {
    	//System.out.println("リージョンId=" + regionId + ", travelTime=" + travelTime);
    	//System.out.println("リクエスト呼ばれてるか確認："+spotId);
       	List<Region>regions=regionRepo.searchRegions
    	(countryName,regionId,regionName,spotId,travelTime,budget);
    	
    	return regions.stream()
    	    	.map(r -> Map.of(
    	                "id", r.getId(),
    	                "name", r.getName(),
    	                "budget", r.getBudget(),
    	                "flightTime", r.getFlightTime(),
    	                "spots", r.getTouristSpots() != null
    	                ? r.getTouristSpots().stream()
    	                .map(TouristSpot::getName)
    	                .collect(Collectors.toList()): List.of(),
    	                "countryName", r.getCountry() != null 
    	                ? r.getCountry().getName() : null         
    	             ))
    	         .collect(Collectors.toList());
    	    }
    @GetMapping("/travel-times") // 渡航時間リスト
    public List<Integer>getTravelTimes(){
    	return regionRepo.findAll().stream() //逐次処理用にデータをバラす
    			.map(Region::getFlightTime)
    			.distinct() //重複防止
    			.sorted()   //昇り順で並べる
    			.collect(Collectors.toList()); //バラしたデータをListにまたまとめる
    }
    
    @GetMapping("/budgets") // 予算リスト
    public List<Integer> getBudgets(){
    	return regionRepo.findAll().stream()
    			.map(Region::getBudget)
    			.distinct() //重複防止
    			.sorted()   //昇り順で並べる
    			.collect(Collectors.toList());
    }
    
}
