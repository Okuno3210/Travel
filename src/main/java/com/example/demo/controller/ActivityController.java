package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/activity") // すべてのルートに /scenic を共通プレフィックスとして付与
public class ActivityController {

    @GetMapping("")
    public String showActivityPage() {
        return "activity/activity1"; // テンプレート: templates/scenic/scenic1.html
    }

    @GetMapping("/activity2")
    public String showActivity2Page() {
        return "activity/activity2";
    }

    @GetMapping("/activity3")
    public String showActivity3Page() {
        return "activity/activity3";
    }

    @GetMapping("/activity4")
    public String showActivity4Page() {
        return "activity/activity4";
    }
}
