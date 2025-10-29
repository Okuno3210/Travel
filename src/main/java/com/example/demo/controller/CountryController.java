package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Country;
import com.example.demo.service.CountryService;

@Controller
public class CountryController {

    private final CountryService countryService; // フィールド

    // コンストラクタで注入（推奨）
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }
	@GetMapping("/EGY")
	public String EgyptView(Model model) {	
		Country country = countryService.findByCode("EGY");
	    model.addAttribute("country", country);
		return "/countrys/egy";
	}
	
	@GetMapping("/AUS")
	public String austrariaView(Model model) {
	    Country country = countryService.findByCode("AUS");
	    model.addAttribute("country", country);
	    return "countrys/aus";
	}

	
	@GetMapping("/USA")
	public String UsaView(Model model) {
		Country country = countryService.findByCode("USA");
	    model.addAttribute("country", country);
		return "/countrys/usa";
	}
	
	@GetMapping("/IRN")
	public String IranView(Model model) {	
		Country country = countryService.findByCode("IRN");
	    model.addAttribute("country", country);
		return "/countrys/irn";
	}
	
	
	@GetMapping("/ITA")
	public String ItalyView(Model model) {
	    Country country = countryService.findByCode("ITA");
	    model.addAttribute("country", country);
	    return "countrys/ita";
	}
	
	@GetMapping("/FRA")
	public String FranceView(Model model) {	
		Country country = countryService.findByCode("FRA");
	    model.addAttribute("country", country);
		return "/countrys/fra";
	}
	
	@GetMapping("/VNM")
	public String VietnamView(Model model) {	
		Country country = countryService.findByCode("VNM");
	    model.addAttribute("country", country);
		return "/countrys/vnm";
	}
	
	@GetMapping("/CHE")
	public String SwitzerlandView(Model model) {	
		Country country = countryService.findByCode("CHE");
	    model.addAttribute("country", country);
		return "/countrys/che";
	}
	
	@GetMapping("/THA")
	public String ThaiView(Model model) {	
		Country country = countryService.findByCode("THA");
	    model.addAttribute("country", country);
		return "/countrys/tha";
	}
	
	@GetMapping("/IND")
	public String IndiaView(Model model) {	
		Country country = countryService.findByCode("IND");
	    model.addAttribute("country", country);
		return "/countrys/ind";
	}
	
	@GetMapping("/CHN")
	public String ChinaView(Model model) {	
		Country country = countryService.findByCode("CHN");
	    model.addAttribute("country", country);
		return "/countrys/chn";
	}
	
	@GetMapping("/RUS")
	public String RussiaView(Model model) {	
		Country country = countryService.findByCode("RUS");
	    model.addAttribute("country", country);
		return "/countrys/rus";
	}
	
	@GetMapping("/KOR")
	public String KoreaView(Model model) {	
		Country country = countryService.findByCode("KOR");
	    model.addAttribute("country", country);
		return "/countrys/kor";
	}


}
