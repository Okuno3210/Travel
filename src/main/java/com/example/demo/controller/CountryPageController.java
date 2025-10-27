package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Country;
import com.example.demo.service.CountryService;

@Controller
@RequestMapping("/countrys")
public class CountryPageController {

    private final CountryService countryService;

    public CountryPageController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/{template}")
    public String showCountryPage(@PathVariable String template, Model model) {
        // template は小文字なので大文字に変換して検索（DB code が大文字の場合）
        Country country = countryService.findByCode(template.toUpperCase());
        if (country != null) {
            model.addAttribute("country", country);
        }
        return "countrys/" + template; // templates/countrys/{template}.html
    }
}
