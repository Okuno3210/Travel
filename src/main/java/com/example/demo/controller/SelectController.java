package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Country;
import com.example.demo.entity.Region;
import com.example.demo.service.SelectService;

@Controller
@RequestMapping("/select")
public class SelectController {

    private final SelectService selectService;

    public SelectController(SelectService selectService) {
        this.selectService = selectService;
    }

    /**
     * ✅ 初期表示（全件表示）
     * HTTP GET /select
     */
    @GetMapping("")
    public String showSelectPage(Model model) {
        // Serviceから全地域データを取得
        List<Region> regions = selectService.getAllRegions();

        // モデルに追加
        model.addAttribute("results", regions);
        model.addAttribute("budget", "");
        model.addAttribute("time", "");
        model.addAttribute("timezone", "");

        return "select"; // select.html を表示
    }

    /**
     * ✅ フィルタリング処理
     * HTTP GET /select/filter?budget=low&time=short&timezone=asia
     */
    @GetMapping("/filter")
    public String filter(
            @RequestParam(defaultValue = "") String budget,
            @RequestParam(defaultValue = "") String time,
            @RequestParam(defaultValue = "") String timezone,
            @RequestParam(defaultValue = "") String concept,
            Model model) {

        List<Region> results = selectService.getFilteredRegions(budget, time, timezone,concept);

        model.addAttribute("results", results);
        model.addAttribute("budget", budget);
        model.addAttribute("time", time);
        model.addAttribute("timezone", timezone);
        model.addAttribute("concept", concept);

        return "select"; // ← select.html に戻す
    }

    /**
     * ✅ 国ごとの詳細ページ表示
     */
    @GetMapping("/country/{id}/regions")
    public String getCountryDetail(@PathVariable Long id, Model model) {
        Country country = selectService.getAllCountries().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (country == null) {
            return "error/404";
        }

        List<Region> regions = selectService.getAllRegions().stream()
                .filter(r -> r.getCountry().getId().equals(id))
                .toList();

        model.addAttribute("country", country);
        model.addAttribute("regions", regions);

        return "country-detail"; // country-detail.html に対応
    }
}