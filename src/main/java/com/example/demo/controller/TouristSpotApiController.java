package com.example.demo.controller; //10/30,11/2修正

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.TouristSpotRepository;
@RestController
@RequestMapping("/api/tourist-spots")
public class TouristSpotApiController {
	private final TouristSpotRepository touristSpotRepo;
	
	public TouristSpotApiController(TouristSpotRepository touristSpotRepo) {
		this.touristSpotRepo=touristSpotRepo;
	}
	
	@GetMapping("/options")
    public List<Map<String,Object>> getOptions(){
    	return touristSpotRepo.findAll().stream()
    			.map(s ->{Map<String, Object> m = new HashMap<>();
    			m.put("id", s.getId());
    			m.put("name", s.getName());
    			m.put("description", s.getDescription()); //10/30追加
    			m.put("imageUrl",s.getImageUrl());
    			return m;
    				
    			})
    			.collect(Collectors.toList());	
    }

}
