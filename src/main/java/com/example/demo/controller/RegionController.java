package com.example.demo.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.entity.Country;
import com.example.demo.entity.Region;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.RegionRepository;

@Controller
public class RegionController {

    @Autowired
    private RegionRepository regionRepository;
    
    @Autowired
    private CountryRepository countryRepository;

    // 一覧ページ
    @GetMapping("/regions")
    public String getRegions(Model model) {
        List<Region> regions = regionRepository.findAllWithSpots();
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
    
 // Country別の地域一覧
    @GetMapping("/country/{countryId}/regions")
    public String getRegionsByCountry(@PathVariable Long countryId, Model model) {
    	 Country country = countryRepository.findById(countryId)
                 .orElseThrow(() -> new IllegalArgumentException("Invalid country id: " + countryId));
    	
    	List<Region> regions = regionRepository.findByCountryId(countryId);
        model.addAttribute("regions", regions);
        model.addAttribute("country", country);
        
        String countryCode = country.getCode();
        if (countryCode == null || countryCode.isEmpty()) {
            // fallback: 国名をローマ字・英字小文字に変換して使用
            countryCode = country.getName().toLowerCase(Locale.ROOT)
                    .replace("（", "")
                    .replace("）", "")
                    .replace(" ", "");
        }
        return "country"; // → templates/regionsByCountry.html（新規または再利用）
    }

}
