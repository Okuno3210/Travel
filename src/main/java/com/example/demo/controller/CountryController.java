package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CountryController {
	
	@GetMapping("/EGY")
	public String EgyptView() {	
		return "egy";
	}
	
	@GetMapping("/AUS")
	public String AustrariaView() {	
		return "aus";
	}
	
	@GetMapping("/USA")
	public String UsaView() {	
		return "usa";
	}
	
	@GetMapping("/IRN")
	public String IranView() {	
		return "irn";
	}
	
	
	@GetMapping("/ITA")
	public String ItalyView() {	
		return "ita";
	}
	
	@GetMapping("/FRA")
	public String FranceView() {	
		return "fra";
	}
	
	@GetMapping("/VNM")
	public String VietnamView() {	
		return "vnm";
	}
	
	@GetMapping("/CHE")
	public String SwitzerlandView() {	
		return "che";
	}
	
	@GetMapping("/THA")
	public String ThaiView() {	
		return "tha";
	}
	
	@GetMapping("/IND")
	public String IndiaView() {	
		return "ind";
	}
	
	@GetMapping("/CHN")
	public String ChinaView() {	
		return "chn";
	}
	
	@GetMapping("/RUS")
	public String RussiaView() {	
		return "rus";
	}
	
	@GetMapping("/KOR")
	public String KoreaView() {	
		return "kor";
	}

}
