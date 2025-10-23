package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CountryController {
	
	@GetMapping("/EGY")
	public String EgyptView() {	
		return "/countrys/egy";
	}
	
	@GetMapping("/AUS")
	public String AustrariaView() {	
		return "/countrys/aus";
	}
	
	@GetMapping("/USA")
	public String UsaView() {	
		return "/countrys/usa";
	}
	
	@GetMapping("/IRN")
	public String IranView() {	
		return "/countrys/irn";
	}
	
	
	@GetMapping("/ITA")
	public String ItalyView() {	
		return "/countrys/ita";
	}
	
	@GetMapping("/FRA")
	public String FranceView() {	
		return "/countrys/fra";
	}
	
	@GetMapping("/VNM")
	public String VietnamView() {	
		return "/countrys/vnm";
	}
	
	@GetMapping("/CHE")
	public String SwitzerlandView() {	
		return "/countrys/che";
	}
	
	@GetMapping("/THA")
	public String ThaiView() {	
		return "/countrys/tha";
	}
	
	@GetMapping("/IND")
	public String IndiaView() {	
		return "/countrys/ind";
	}
	
	@GetMapping("/CHN")
	public String ChinaView() {	
		return "/countrys/chn";
	}
	
	@GetMapping("/RUS")
	public String RussiaView() {	
		return "/countrys/rus";
	}
	
	@GetMapping("/KOR")
	public String KoreaView() {	
		return "/countrys/kor";
	}

}
