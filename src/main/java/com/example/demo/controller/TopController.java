package com.example.demo.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class TopController {
	
	@GetMapping("/")
	public String showTopPage(Model model) throws IOException, URISyntaxException {

	    // resources/static/images/tourist_spot のパス取得
	    Path path = Paths.get(getClass().getResource("/static/images/tourist_spot").toURI());

	    // 画像一覧を取得
	    List<String> allImages = Files.list(path)
	            .map(p -> p.getFileName().toString())
	            .filter(name -> name.endsWith(".jpg") || name.endsWith(".png"))
	            .collect(Collectors.toList());

	    // ランダムに3枚選択
	    Collections.shuffle(allImages);
	    List<String> randomImages = allImages.stream().limit(3).collect(Collectors.toList());

	    // ★ 画像ファイル名から番号を取り出してIDに変換
	    List<Integer> imageIds = randomImages.stream()
	            .map(img -> Integer.parseInt(img.replaceAll("\\D", "")))  // 数字だけ抜き出す
	            .collect(Collectors.toList());

	    // Thymeleafへ渡す
	    model.addAttribute("randomImages", randomImages);
	    model.addAttribute("imageIds", imageIds);

	    return "top";  // top.html を返す
	}

	
//	@GetMapping("/list")
//	public String mypageView() {
//		return "list";
//	}
	
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
