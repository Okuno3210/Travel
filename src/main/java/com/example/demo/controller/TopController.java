package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopController {
	
	@GetMapping("/")
	public String toppageView() {	
		return "top";
	}
	
//	@GetMapping("/list")
//	public String mypageView() {
//		return "list";
//	}
	
	@GetMapping("/login")
	public String loginView() {
		return "login";
	}
	
	@GetMapping("/select")
	public String selectView() {
		return "select";
	}
	
	@GetMapping("/maplink")
	public String mapView() {
		return "maplink";
	}
	
}
