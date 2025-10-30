//package com.example.demo.controller; // ← あなたのプロジェクトのパッケージ名に合わせて変更
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/region")
//public class RegionController {
//	
//	 @GetMapping("/{code}")
//	    public String showRegion(@PathVariable String code) {
//	        // 例: "los" や "las" などのコードで地域情報を取得
//
//	        return "region/" + code ;  // 共通テンプレート
//	    }
//}
package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.entity.Region;
import com.example.demo.repository.RegionRepository;

@Controller
public class RegionController {

    @Autowired
    private RegionRepository regionRepository;

    // 一覧ページ
    @GetMapping("/regions")
    public String getRegions(Model model) {
        List<Region> regions = regionRepository.findAll();
        model.addAttribute("regions", regions);
        return "regions"; // → templates/regions.html
    }

    // 個別ページ
    @GetMapping("/region/{id}")
    public String getRegion(@PathVariable Long id, Model model) {
        Region region = regionRepository.findById(id)
                         .orElseThrow(() -> new IllegalArgumentException("Invalid region id: " + id));
        model.addAttribute("region", region);
        return "regionDetail"; // → templates/regionDetail.html
    }
}
