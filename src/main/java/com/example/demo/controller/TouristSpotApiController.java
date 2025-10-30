package com.example.demo.controller; //10/30修正

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
    			.map(s ->{Map<String, Object> m = new HashMap<>();
    			m.put("id", s.getId());
    			m.put("name", s.getName());
    			m.put("description", s.getDescription()); //10/30追加
    			return m;
    				
    			})
    			.collect(Collectors.toList());	
    }

}
