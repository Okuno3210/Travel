package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.CountryRepository;

@RestController
@RequestMapping("/api/countries")
public class CountryApiController {
	private final CountryRepository countryRepo;
	public CountryApiController(CountryRepository countryRepo) {
		this.countryRepo=countryRepo;
	}
	@GetMapping("/options")
    public List<Map<String,Object>> getOptions(){
    	return countryRepo.findAll().stream()
    			.map(c ->{Map<String, Object> m = new HashMap<>();
    			m.put("id",c.getId());
    			m.put("code", c.getCode()); //11/4追加
    			m.put("name", c.getName());
    			m.put("description",c.getDescription());
    			m.put("ImageUrl", c.getImageUrl()); //11/4追加
    			return m;
    			})
    			.collect(Collectors.toList());	
    }
}
