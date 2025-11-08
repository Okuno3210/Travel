
package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.TourRepository;

@RestController
@RequestMapping("/api/tour")
public class TourApiController {
	private final TourRepository tourRepo;
	
	public TourApiController(TourRepository tourRepo) {
		this.tourRepo=tourRepo;
	}
	
	@GetMapping("/options") //データ取得
	@ResponseBody
    public List<Map<String,Object>> getOptions(){
    	return tourRepo.findAll().stream()
    			.map(to ->{Map<String, Object> m = new HashMap<>();
    			//m.put("id", to.getId()); 自動生成用にエンティティに@GeneratedValue
    			m.put("countryId", to.getCountryId());
    			m.put("countryName", to.getCountryName());
    			m.put("title", to.getTitle());
    			m.put("description", to.getDescription());
    			m.put("basePrice", to.getBasePrice());
    			m.put("schedule", to.getSchedule());
    			m.put("imageUrl", to.getImageUrl());
    			return m;
    				
    			})
    			.collect(Collectors.toList());	
    }
}