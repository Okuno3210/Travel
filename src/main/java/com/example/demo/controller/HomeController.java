package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {
	@GetMapping("/usa")
	public String showUsa() {
		return "countrys/usa"; //usa.htmlを表示
	}

}
// springboot起動してログインしたトップページで http://localhost:8080/usa