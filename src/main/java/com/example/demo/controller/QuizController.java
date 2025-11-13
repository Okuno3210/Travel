package com.example.demo.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Country;
import com.example.demo.entity.Food;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.FoodRepository;

@RestController
@RequestMapping("/api")
public class QuizController {

    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private FoodRepository foodRepo;

    @GetMapping("/quiz")
    public Map<String,Object> getRandomQuiz(HttpSession session) {
        Random rand = new Random();

        // --- クイズタイプをランダムに選ぶ ---
        String type = rand.nextBoolean() ? "country" : "food";

        Set<Long> usedCountryIds = (Set<Long>) session.getAttribute("usedCountryIds");
        Set<Long> usedFoodIds = (Set<Long>) session.getAttribute("usedFoodIds");
        if(usedCountryIds == null) usedCountryIds = new HashSet<>();
        if(usedFoodIds == null) usedFoodIds = new HashSet<>();

        List<?> list;
        Set<Long> usedIds;
        String questionText;

        if(type.equals("country")) {
            list = countryRepo.findAll();
            usedIds = usedCountryIds;
            questionText = "この国はどれでしょう？";
        } else {
            list = foodRepo.findAll();
            usedIds = usedFoodIds;
            questionText = "この料理はどれでしょう？";
        }

        // 未出題のリストに絞る
        List<?> remaining = list.stream()
                .filter(e -> {
                    if(type.equals("country")) return !usedIds.contains(((Country)e).getId());
                    else return !usedIds.contains(((Food)e).getId());
                })
                .collect(Collectors.toList());

        // すべて出題済みならリセット
        if(remaining.isEmpty()) {
            usedIds.clear();
            remaining = list;
        }

        Object selected = remaining.get(rand.nextInt(remaining.size()));

        // 出題済みに追加
        if(type.equals("country")) usedIds.add(((Country)selected).getId());
        else usedIds.add(((Food)selected).getId());

        if(type.equals("country")) session.setAttribute("usedCountryIds", usedIds);
        else session.setAttribute("usedFoodIds", usedIds);

        // 選択肢作成
        List<String> options = list.stream()
                .filter(e -> {
                    if(type.equals("country")) return !((Country)e).getId().equals(((Country)selected).getId());
                    else return !((Food)e).getId().equals(((Food)selected).getId());
                })
                .map(e -> type.equals("country") ? ((Country)e).getName() : ((Food)e).getName())
                .collect(Collectors.toList());

        Collections.shuffle(options);
        options = options.subList(0, Math.min(3, options.size()));
        options.add(type.equals("country") ? ((Country)selected).getName() : ((Food)selected).getName());
        Collections.shuffle(options);

        Map<String,Object> quiz = new HashMap<>();
        quiz.put("id", type.equals("country") ? ((Country)selected).getId() : ((Food)selected).getId());
        quiz.put("question", questionText);
        quiz.put("description", type.equals("country") ? ((Country)selected).getDescription() : ((Food)selected).getDescription());
        quiz.put("options", options);
        quiz.put("answer", type.equals("country") ? ((Country)selected).getName() : ((Food)selected).getName());
        quiz.put("imageUrl", type.equals("country") ? ((Country)selected).getImageUrl() : ((Food)selected).getImageUrl());

        return quiz;
    }
}
