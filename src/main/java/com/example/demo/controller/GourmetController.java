package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GourmetController {
    @GetMapping("/gourmet")
    public String showGourmetPage() {
        return "/grume/gourmet"; // templates/gourmet.html が必要
    }
    
    @GetMapping("/gourmet2")
    public String showGourmet2Page() {
        return "/grume/gourmet2"; // templates/gourmet.html が必要
    }
}

