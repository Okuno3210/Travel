package com.example.demo.service; //厚田10/30修正

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Concept;
import com.example.demo.entity.Country;
import com.example.demo.entity.Food;
import com.example.demo.entity.Region;
import com.example.demo.entity.TouristSpot;
import com.example.demo.repository.ConceptRepository;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.FoodRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.TouristSpotRepository;

@Component
public class DataLoader implements ApplicationRunner{
   
	private final CountryRepository countryRepo;
	private final RegionRepository regionRepo;
    private final TouristSpotRepository spotRepo;
    private final FoodRepository foodRepo;
    private final ConceptRepository conceptRepo;
    //CSV増えたら書き足す
    
    // 2. コンストラクタ（final フィールドの初期化）
    public DataLoader(CountryRepository countryRepo, 
    RegionRepository regionRepo, TouristSpotRepository spotRepo, 
    FoodRepository foodRepo,ConceptRepository conceptRepo) {
        this.regionRepo = regionRepo;
        this.countryRepo = countryRepo;
        this.spotRepo = spotRepo;
        this.foodRepo = foodRepo;
        this.conceptRepo = conceptRepo;
    }//CSV増えたら書き足す

    @Value("classpath:data/country.csv")
    private Resource countriesCsv;
    
    @Value("classpath:data/region.csv")
    private Resource regionsCsv;
    
    @Value("classpath:data/tourist_spot.csv")
    private Resource spotsCsv;
    
    @Value("classpath:data/food.csv")
    private Resource foodsCsv;
    
    @Value("classpath:data/concept.csv")
    private Resource conceptsCsv;
    
    @Value("classpath:data/region_concept.csv")
    private Resource regionConceptsCsv;

    //CSV増えたら書き足す

    // @PostConstruct 　1027削除、選択肢が二重になる
    private void loadCsv() {
    	loadCountries(); //countryを一番に読み込み
    	loadRegions();   //2番めにcountryに依存してるregionを読み込む
    	var regions = regionRepo.findAll()
    	     .stream()
    		 .collect(Collectors.toMap(Region::getId, r -> r));
    	loadSpots(regions);     //3番目以降はconcept以外regionに依存 
        loadFoods(regions);
        loadConcepts();
        loadRegionConcepts();
    }
    
//★ CSV増加時はここに loadXxx() を追加

    private void loadCountries() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader
        		(countriesCsv.getInputStream(), StandardCharsets.UTF_8))) {
            br.lines().skip(1)
            .filter(line -> !line.trim().isEmpty()) // ← 空行を除外
            .forEach(line -> {
                String[] arr = line.split(",");
                if (arr.length < 5) return;   // 列数が足りなければスキップ
                Country c = new Country();
                c.setCode(arr[1]);
                c.setName(arr[2]);
                c.setDescription(arr[3]);
                c.setImgUrl(arr[4]);
                countryRepo.save(c); //カントリー.csvのidエンティティで自動生成して割り振られる  
                
            });
        } catch (IOException e) { e.printStackTrace(); }
    }
    
    private void loadRegions() {
        try (BufferedReader br = new BufferedReader
        (new InputStreamReader(regionsCsv.getInputStream(), 
        				StandardCharsets.UTF_8))) {
            br.lines()
            .skip(1) // ヘッダーをスキップ
            .filter(line -> !line.trim().isEmpty()) // ← 空行を除外
            .forEach(line -> {
                String[] arr = line.split(",");
                if (arr.length < 9) return;   // 列数が足りなければスキップ
                Region r = new Region();
                //r.setId(Long.parseLong(arr[0])); //region.csvは主キーのみ、自動生成
                r.setName(arr[2]);
                r.setBudget(arr[3]);
                r.setFlightTime(arr[4]);
                r.setTimezone(arr[5]);
                r.setClimate(arr[6]);
                r.setRiskLevel(arr[7]);

                r.setDescription(arr[8]);
                r.setImageUrl(arr[9]); 
                //Country country = countryRepo.findById(Long.parseLong(arr[1])).orElse(null); 10/30書き換え
                Country country = countryRepo.findById(Long.parseLong(arr[1])).orElse(null);
                if (country != null) {
                    r.setCountry(country);
                    regionRepo.save(r);
                } else {
                    System.out.println("⚠️ Country not found for region: " + arr[0] + ", country_id=" + arr[1]);
                }

            });
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadSpots(Map<Long, Region> regionCache) {
        try (BufferedReader br = new BufferedReader
    (new InputStreamReader(spotsCsv.getInputStream(), StandardCharsets.UTF_8))) {
            br.lines().skip(1)
            .filter(line -> !line.trim().isEmpty()) // ← 空行を除外
            .forEach(line -> {
                String[] arr = line.split(",");
                if(arr.length > 5) {
                System.out.println("Line length: " + arr.length + " -> " + line);
                }
                TouristSpot s = new TouristSpot();
                //s.setId(Long.parseLong(arr[0]));　主キー自動生成
                s.setName(arr[2]);
                s.setDescription(arr[3]);
                Region region = regionCache.get(Long.parseLong(arr[1]));
                if (region != null) {
                    s.setRegion(region);
                }
                s.setImgUrl(arr[4]);
                spotRepo.save(s);
            });
        } catch (IOException e) { e.printStackTrace(); }
    }
    
	private void loadFoods(Map<Long, Region> regionCache) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(foodsCsv.getInputStream(), StandardCharsets.UTF_8))) {

            br.lines().skip(1) // ヘッダーをスキップ
              .filter(line -> !line.trim().isEmpty())
              .forEach(line -> {
                  String[] arr = line.split(","); // descriptionにカンマがあってもOK

                  // Foodエンティティ作成
                  Food food = new Food();
                  food.setName(arr[2]);
                  food.setDescription(arr.length > 3 ? arr[3] : "");
                  food.setImageUrl(arr.length > 4 ? arr[4] : "");

                  // Regionを紐づけ
                  Region region = regionCache.get(Long.parseLong(arr[1].trim()));
                  if (region != null) {
                      food.setRegion(region);
                  }
                  
                  // 保存
                  foodRepo.save(food);
              });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	private void loadConcepts() {
		try (BufferedReader br = new BufferedReader
			    (new InputStreamReader(conceptsCsv.getInputStream(), StandardCharsets.UTF_8))) {
			 br.lines()
	            .skip(1) // ヘッダーをスキップ
	            .filter(line -> !line.trim().isEmpty()) // ← 空行を除外
	            .forEach(line -> {
	                String[] arr = line.split(",");
	                Concept co = new Concept();
	                co.setName(arr[1].trim());
	                conceptRepo.save(co);
	            });
        } catch (IOException e) { e.printStackTrace(); }
    }
	
	private void loadRegionConcepts() {
	    try (BufferedReader br = new BufferedReader(
	            new InputStreamReader(regionConceptsCsv.getInputStream(), StandardCharsets.UTF_8))) {

	        br.lines().skip(1) // ヘッダー行スキップ
	          .filter(line -> !line.trim().isEmpty())
	          .forEach(line -> {
	              String[] arr = line.split(",");
	              if (arr.length < 3) return; // id, region_id, concept_id → 最低3列必要

	              Long regionId = Long.parseLong(arr[1].trim());
	              Long conceptId = Long.parseLong(arr[2].trim());

	              Region region = regionRepo.findById(regionId).orElse(null);
	              Concept concept = conceptRepo.findById(conceptId).orElse(null);

	              if (region != null && concept != null) {
	                  if (region.getConcepts() == null) {
	                      region.setConcepts(new java.util.ArrayList<>());
	                  }

	                  // 既に存在しない場合のみ追加
	                  if (!region.getConcepts().contains(concept)) {
	                      region.getConcepts().add(concept);
	                      regionRepo.save(region);
	                  }
	              }
	          });

	        System.out.println("✅ region_concept.csv の読み込み完了");

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}



    
    //新しいCSVファイル用に private void ファイル名() { try-catch文でメソッドを書き足す
	@Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception{
    	 //H2をfileにした後、選択肢を毎回新規取得するために一旦捨てる　10/30追加
    	conceptRepo.deleteAllInBatch();
    	foodRepo.deleteAllInBatch();
    	spotRepo.deleteAllInBatch();
    	regionRepo.deleteAllInBatch();
    	countryRepo.deleteAllInBatch();
        
    	loadCsv(); //1027追加

    }

    
}
