package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.entity.Food;
import com.example.demo.repository.FoodRepository;

@Controller
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    // 一覧ページ
    @GetMapping("/foods")
    public String getFoods(Model model) {
        List<Food> foods = foodRepository.findAll();
        model.addAttribute("foods", foods);
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
