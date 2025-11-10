package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/activity") // すべてのルートに /scenic を共通プレフィックスとして付与
public class ActivityController {

    @GetMapping("")
    public String showActivityPage() {
        return "activity/activity"; // テンプレート: templates/scenic/scenic1.html
    }

    @GetMapping("/theme park")
    public String showthemeParkPage() {
        return "activity/theme park";
    }

    @GetMapping("/museum")
    public String showMuseumPage() {
        return "activity/museum";
    }

    @GetMapping("/leisure")
    public String showLeisurePage() {
        return "activity/leisure";
    }
}
