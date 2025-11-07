package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GourmetController {
	
	@GetMapping("/gourmet")
    public String showGourmetPage() {
        return "grume/gourmet"; // ← templates/grume/meat.html に対応
    }

    // /grume/meat にアクセスしたら templates/grume/meat.html を表示
    @GetMapping("/grume/meat")
    public String showMeatPage() {
        return "grume/meat"; // ← templates/grume/meat.html に対応
    }
    // /grume/meat にアクセスしたら templates/grume/meat.html を表示
    @GetMapping("/grume/ohters")
    public String showOhtersPage() {
        return "grume/ohters"; // ← templates/grume/meat.html に対応
    }
    // /grume/meat にアクセスしたら templates/grume/meat.html を表示
    @GetMapping("/grume/seafood")
    public String showSeafoodPage() {
        return "grume/seafood"; // ← templates/grume/meat.html に対応
    }
    // /grume/meat にアクセスしたら templates/grume/meat.html を表示
    @GetMapping("/grume/vegetable")
    public String showVegetablePage() {
        return "grume/vegetable"; // ← templates/grume/meat.html に対応
    }
    // /grume/meat にアクセスしたら templates/grume/meat.html を表示
    @GetMapping("/grume/grains")
    public String showGrainsPage() {
        return "grume/grains"; // ← templates/grume/meat.html に対応
    }
}
