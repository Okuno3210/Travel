package com.example.demo.controller; // ← あなたのプロジェクトのパッケージ名に合わせて変更

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/region")
public class RegionController {
	
	 @GetMapping("/{code}")
	    public String showRegion(@PathVariable String code) {
	        // 例: "los" や "las" などのコードで地域情報を取得

	        return "region/" + code ;  // 共通テンプレート
	    }
}
