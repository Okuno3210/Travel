package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FlightController {

    @GetMapping("/flight/search")
    public String showFlightSearch(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String region,
            Model model) {

        model.addAttribute("country", country);
        model.addAttribute("region", region);
        return "flight-search"; // templates/flight-search.html
    }

    @GetMapping("/flight/result")
    public String showFlightResult(
            @RequestParam String departure,
            @RequestParam String destination,
            @RequestParam String date,
            @RequestParam(required = false) String passenger,
            Model model) {

        // 本来はAPIで航空券データを取得する箇所
        model.addAttribute("departure", departure);
        model.addAttribute("destination", destination);
        model.addAttribute("date", date);
        model.addAttribute("passenger", passenger);
        model.addAttribute("price", 98000); // ダミーデータ
        return "flight-result"; // templates/flight-result.html
    }
}
