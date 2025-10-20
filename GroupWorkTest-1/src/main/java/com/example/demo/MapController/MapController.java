package com.example.demo.MapController;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Entity.Location;
@Controller
public class MapController {
	
	private static final List<Location> list = Arrays.asList(
		        // 地図画像に合わせた座標の初期データ（名称, 経度, 緯度）
		        
		    new Location("アメリカ",-95.7,37.0),
		    	
		    new Location("日本",135.0,35.0)
		    );

	//private final List<Location> list=new ArrayList<Location>();
	
    @GetMapping("/map")
    public String mapPage() {
        return "map"; // src/main/resources/templates/map.html をレンダリング
    }
	
    @GetMapping("/api/locations")
    @ResponseBody
    public List<Location> getAllLocations() {
        return list; // 静的リストを返却
    }
    
}
