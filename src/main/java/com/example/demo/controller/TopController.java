package com.example.demo.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopController {


	@GetMapping("/")
	public String toppageView(Model model) {
	    try {
	        File folder = new ClassPathResource("static/images/tourist_spot").getFile();
	        String[] files = folder.list((dir, name) ->
	                name.toLowerCase().endsWith(".jpg") ||
	                name.toLowerCase().endsWith(".jpeg") ||
	                name.toLowerCase().endsWith(".png")
	        );

	        if (files != null && files.length > 0) {
	            List<String> imageList = new ArrayList<>();
	            for (String file : files) {
	                imageList.add("/images/tourist_spot/" + file);
	            }

	            Collections.shuffle(imageList);
	            model.addAttribute("randomImages", imageList.subList(0, Math.min(5, imageList.size())));
	        } else {
	            System.out.println("⚠️ 画像フォルダが空、または見つかりません。");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return "top";
	}


    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/select")
    public String selectView() {
        return "select";
    }

    @GetMapping("/maplink")
    public String mapView() {
        return "maplink";
    }
}
