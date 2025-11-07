package com.example.demo.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.TouristSpot;
import com.example.demo.repository.TouristSpotRepository;


@Controller
public class TopController {
	
	@Autowired
	private TouristSpotRepository touristSpotRepository;
		
	@GetMapping("/")
	public String showTopPage(Model model) {

	    List<TouristSpot> allSpots = touristSpotRepository.findAll();

	    Collections.shuffle(allSpots);
	    List<TouristSpot> randomSpots = allSpots.stream().limit(3).toList();

	    model.addAttribute("randomSpots", randomSpots);

	    return "top";
	}
	
	@GetMapping("/login")
	public String loginView() {
		return "login";
	}

	
	@GetMapping("/maplink")
	public String mapView() {
		return "maplink";
	}
	
}
