package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.entity.Food;
import com.example.demo.repository.FoodRepository;

@Controller
public class FoodController {
	
	// 例: Food.java
	private String region; // 例えば "アジア", "ヨーロッパ" など

    @Autowired
    private FoodRepository foodRepository;

    // 一覧ページ
    @GetMapping("/foods")
    public String getFoods(Model model) {
        List<Food> foods = foodRepository.findAll();

        // 地域ごとにグループ化
        Map<String, List<Food>> foodsByRegion = foods.stream()
        	    .collect(Collectors.groupingBy(food -> food.getRegion().getName()));

        model.addAttribute("foodsByRegion", foodsByRegion);
        return "foods"; // templates/foods.html
    }


    // 個別ページ
    @GetMapping("/foods/{id}")
    public String getFood(@PathVariable Long id, Model model) {
        Food food = foodRepository.findById(id)
                     .orElseThrow(() -> new IllegalArgumentException("Invalid food id: " + id));
        model.addAttribute("food", food);
        return "foodDatails"; // templates/foodDetail.html
    }
}
