package com.example.demo.controller;

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
	private final TouristSpotRepository spotRepo;
	
	public TouristSpotApiController(TouristSpotRepository spotRepo) {
		this.spotRepo=spotRepo;
	}
	
	@GetMapping("/options")
    public List<Map<String,Object>> getOptions(){
    	return spotRepo.findAll().stream()
    			/*.filter(s -> s.getId() != null && s.getName() != null)
    			.map(s -> Map.of("id", s.getId(), "name", s.getName()))*/
    			.map(s ->{Map<String, Object> m = new HashMap<>();
    			m.put("id", s.getId());
    			m.put("name", s.getName());
    			return m;
    				
    			})
    			.collect(Collectors.toList());	
    }

}
