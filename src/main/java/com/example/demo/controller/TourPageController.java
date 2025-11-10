package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//★ データ構造であるエンティティをインポート
import com.example.demo.entity.TourEntity;
//★ データベース操作のためのリポジトリをインポート
import com.example.demo.repository.TourRepository; 
@Controller
public class TourPageController {// tour.html呼ばれた時用
	
	   // TourRepositoryをフィールドとして定義 (DIの準備)
    private final TourRepository tourRepo; 

    // コンストラクタインジェクションでリポジトリを受け取る
    public TourPageController(TourRepository tourRepo) {
        this.tourRepo = tourRepo;
    }

//@GetMapping(path = "/plantrip", produces = "text/html")
@GetMapping("/plantrip")
public String showTourPage(Model model) {
	
    // 1. データベースから全ツアーリストを取得
    List<TourEntity> tourList = tourRepo.findAll();
    
    // 2. 取得したリストを "to" という名前でモデルに格納
    //    (HTMLの th:each="tour2 : ${to}" の ${to} と名前を合わせる)
    model.addAttribute("to", tourList);
    
    return "tour";}
}
