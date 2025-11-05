package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/scenic") // すべてのルートに /scenic を共通プレフィックスとして付与
public class ScenicController {

    @GetMapping("")
    public String showScenic1Page() {
        return "scenic/scenic1"; // テンプレート: templates/scenic/scenic1.html
    }

    @GetMapping("/scenic2")
    public String showScenic2Page() {
        return "scenic/scenic2";
    }

    @GetMapping("/scenic3")
    public String showScenic3Page() {
        return "scenic/scenic3";
    }

    @GetMapping("/scenic4")
    public String showScenic4Page() {
        return "scenic/scenic4";
    }
}
