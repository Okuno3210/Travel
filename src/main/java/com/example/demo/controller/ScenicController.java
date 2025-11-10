package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/scenic") // すべてのルートに /scenic を共通プレフィックスとして付与
public class ScenicController {

    @GetMapping("")
    public String showScenicPage() {
        return "scenic/scenic"; // テンプレート: templates/scenic/scenic1.html
    }

    @GetMapping("/mountain")
    public String showMountainPage() {
        return "scenic/mountain";
    }

    @GetMapping("/sea")
    public String showSeaPage() {
        return "scenic/sea";
    }

    @GetMapping("/world heitage")
    public String showworldHeitagePage() {
        return "scenic/world heitage";
    }
}
