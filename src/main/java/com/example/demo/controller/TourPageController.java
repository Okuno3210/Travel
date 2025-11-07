package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class TourPageController {// tour.html呼ばれた時用

//@GetMapping(path = "/plantrip", produces = "text/html")
@GetMapping("/plantrip")
public String showTourPage() {
    return "tour";}
}
