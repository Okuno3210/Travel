package com.example.demo.service;

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

import com.example.demo.entity.Airport;
import com.example.demo.entity.Concept;
import com.example.demo.entity.Country;
import com.example.demo.entity.Food;
import com.example.demo.entity.JpAirport;
import com.example.demo.entity.Region;
import com.example.demo.entity.RegionAirport;
import com.example.demo.entity.TouristSpot;
import com.example.demo.repository.AirportRepository;
import com.example.demo.repository.ConceptRepository;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.FoodRepository;
import com.example.demo.repository.JpAirportRepository;
import com.example.demo.repository.RegionAirportRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.TouristSpotRepository;

/**
 * 🌏 データローダー完全版（2025対応）
 * - 各CSVファイルからDBへ初期データをロード
 * - 既存データ削除→再読込
 */
@Component //エクリプスで起動する時は有効にする
public class DataLoader implements ApplicationRunner {

    // ===== Repository定義 =====
    private final CountryRepository countryRepo;
    private final RegionRepository regionRepo;
    private final TouristSpotRepository spotRepo;
    private final FoodRepository foodRepo;
    private final ConceptRepository conceptRepo;
    private final AirportRepository airportRepo;
    private final RegionAirportRepository regionAirportRepo;
    private final JpAirportRepository jpAirportRepo;

    // ===== コンストラクタ =====
    public DataLoader(
            CountryRepository countryRepo,
            RegionRepository regionRepo,
            TouristSpotRepository spotRepo,
            FoodRepository foodRepo,
            ConceptRepository conceptRepo,
            AirportRepository airportRepo,
            RegionAirportRepository regionAirportRepo,
            JpAirportRepository jpAirportRepo) {

        this.countryRepo = countryRepo;
        this.regionRepo = regionRepo;
        this.spotRepo = spotRepo;
        this.foodRepo = foodRepo;
        this.conceptRepo = conceptRepo;
        this.airportRepo = airportRepo;
        this.regionAirportRepo = regionAirportRepo;
        this.jpAirportRepo = jpAirportRepo;
    }

    // ===== CSVリソース設定 =====
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

    @Value("classpath:data/airports.csv")
    private Resource airportsCsv;

    @Value("classpath:data/region_airports.csv")
    private Resource regionAirportsCsv;
    
    @Value("classpath:data/japan_airports.csv")
    private Resource japanAirportsCsv;


    // ==========================
    // 全ロード制御
    // ==========================
    private void loadCsv() {
        loadCountries();
        loadRegions();

        // regionをキャッシュ
        var regions = regionRepo.findAll().stream()
                .collect(Collectors.toMap(Region::getId, r -> r));

        loadSpots(regions);
        loadFoods(regions);
        loadConcepts();
        loadRegionConcepts();
        loadAirports();
        loadRegionAirports(regions);
        loadJpAirports();
    }

    // ==========================
    // 各CSVロードメソッド群
    // ==========================

    private void loadCountries() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                countriesCsv.getInputStream(), StandardCharsets.UTF_8))) {

            br.lines().skip(1).filter(line -> !line.trim().isEmpty())
                    .forEach(line -> {
                        String[] arr = line.split(",");
                        if (arr.length < 5) return;

                        Country c = new Country();
                        //c.setId(Long.parseLong(arr[0])); //エクリプスで起動する時は無効にする 
                        c.setCode(arr[1]);
                        c.setName(arr[2]);
                        c.setDescription(arr[3]);
                        c.setImageUrl(arr[4]);
                        countryRepo.save(c);
                    });

            System.out.println("✅ country.csv 読み込み完了");

        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadRegions() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                regionsCsv.getInputStream(), StandardCharsets.UTF_8))) {

            br.lines().skip(1).filter(line -> !line.trim().isEmpty())
                    .forEach(line -> {
                        String[] arr = line.split(",");
                        if (arr.length < 10) return;

                        Region r = new Region();
                        //r.setId(Long.parseLong(arr[0])); //エクリプスで起動する時は無効にする
                        r.setName(arr[2]);
                        r.setBudget(arr[3]);
                        r.setFlightTime(arr[4]);
                        r.setTimezone(arr[5]);
                        r.setClimate(arr[6]);
                        r.setRiskLevel(arr[7]);
                        r.setDescription(arr[8]);
                        r.setImageUrl(arr[9]);

                        Country country = countryRepo.findById(Long.parseLong(arr[1])).orElse(null);
                        if (country != null) {
                            r.setCountry(country);
                            regionRepo.save(r);
                        }
                    });

            System.out.println("✅ region.csv 読み込み完了");

        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadSpots(Map<Long, Region> regionCache) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                spotsCsv.getInputStream(), StandardCharsets.UTF_8))) {

            br.lines().skip(1).filter(line -> !line.trim().isEmpty())
                    .forEach(line -> {
                        String[] arr = line.split(",");
                        TouristSpot s = new TouristSpot();
                        //s.setId(Long.parseLong(arr[0])); //エクリプスで起動する時は無効にする
                        s.setName(arr[2]);
                        s.setDescription(arr[3]);
                        s.setImageUrl(arr[4]);
                        Region region = regionCache.get(Long.parseLong(arr[1]));
                        if (region != null) s.setRegion(region);
                        spotRepo.save(s);
                    });

            System.out.println("✅ tourist_spot.csv 読み込み完了");

        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadFoods(Map<Long, Region> regionCache) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                foodsCsv.getInputStream(), StandardCharsets.UTF_8))) {

            br.lines().skip(1).filter(line -> !line.trim().isEmpty())
                    .forEach(line -> {
                        String[] arr = line.split(",");
                        Food f = new Food();
                        //f.setId(Long.parseLong(arr[0])); //エクリプスで起動する時は無効にする
                        f.setName(arr[2]);
                        f.setDescription(arr.length > 3 ? arr[3] : "");
                        f.setImageUrl(arr.length > 4 ? arr[4] : "");
                        Region region = regionCache.get(Long.parseLong(arr[1].trim()));
                        if (region != null) f.setRegion(region);
                        foodRepo.save(f);
                    });

            System.out.println("✅ food.csv 読み込み完了");

        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadConcepts() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                conceptsCsv.getInputStream(), StandardCharsets.UTF_8))) {

            br.lines().skip(1).filter(line -> !line.trim().isEmpty())
                    .forEach(line -> {
                        String[] arr = line.split(",");
                        Concept c = new Concept();
                        //c.setId(Long.parseLong(arr[0])); //エクリプスで起動する時は無効にする
                        c.setName(arr[1].trim());
                        conceptRepo.save(c);
                    });

            System.out.println("✅ concept.csv 読み込み完了");

        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadRegionConcepts() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                regionConceptsCsv.getInputStream(), StandardCharsets.UTF_8))) {

            br.lines().skip(1).filter(line -> !line.trim().isEmpty())
                    .forEach(line -> {
                        String[] arr = line.split(",");
                        if (arr.length < 3) return;

                        Long regionId = Long.parseLong(arr[1].trim());
                        Long conceptId = Long.parseLong(arr[2].trim());

                        Region region = regionRepo.findById(regionId).orElse(null);
                        Concept concept = conceptRepo.findById(conceptId).orElse(null);

                        if (region != null && concept != null) {
                            if (region.getConcepts() == null)
                                region.setConcepts(new java.util.ArrayList<>());
                            if (!region.getConcepts().contains(concept)) {
                                region.getConcepts().add(concept);
                                regionRepo.save(region);
                            }
                        }
                    });

            System.out.println("✅ region_concept.csv 読み込み完了");

        } catch (IOException e) { e.printStackTrace(); }
    }

    // ==========================
    // ✈ 空港関連CSV
    // ==========================

    private void loadAirports() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                airportsCsv.getInputStream(), StandardCharsets.UTF_8))) {

            br.lines().skip(1).filter(line -> !line.trim().isEmpty())
                    .forEach(line -> {
                        String[] arr = line.split(",");
                        if (arr.length < 6) return;

                        Airport a = new Airport();
                        //a.setId(Long.parseLong(arr[0])); //エクリプスで起動する時は無効にする
                        a.setName(arr[1].trim());
                        a.setCode(arr[2].trim());
                        
                        Long regionId = Long.parseLong(arr[3].trim());
                        Region region = regionRepo.findById(regionId).orElse(null);
                        a.setRegion(region);
                        
                        Long countryId = Long.parseLong(arr[4].trim());
                        Country country = countryRepo.findById(countryId).orElse(null);
                        a.setCountry(country);
                        
                        a.setDescription(arr[5].trim());
                        
                        airportRepo.save(a);
                    });

            System.out.println("✅ airports.csv 読み込み完了");

        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadRegionAirports(Map<Long, Region> regionCache) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                regionAirportsCsv.getInputStream(), StandardCharsets.UTF_8))) {

            br.lines().skip(1).filter(line -> !line.trim().isEmpty())
                    .forEach(line -> {
                        String[] arr = line.split(",");
                        if (arr.length < 3) return;

                        Long regionId = Long.parseLong(arr[1].trim());
                        Long airportId = Long.parseLong(arr[2].trim());

                        Region region = regionCache.get(regionId);
                        Airport airport = airportRepo.findById(airportId).orElse(null);

                        if (region != null && airport != null) {
                            RegionAirport ra = new RegionAirport();
                            ra.setRegion(region);
                            ra.setAirport(airport);
                            regionAirportRepo.save(ra);
                        }
                    });

            System.out.println("✅ region_airports.csv 読み込み完了");

        } catch (IOException e) { e.printStackTrace(); }
    }
    
    private void loadJpAirports() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                japanAirportsCsv.getInputStream(), StandardCharsets.UTF_8))) {

            br.lines()
              .skip(1) // ヘッダーをスキップ
              .filter(line -> !line.trim().isEmpty())
              .forEach(line -> {
                  String[] arr = line.split(",");
                  if (arr.length < 4) return;

                  try {
                	  JpAirport a = new JpAirport();
                      a.setId(Long.parseLong(arr[0].trim()));  // IDを指定
                      a.setName(arr[1].trim());
                      a.setCode(arr[2].trim());
                      a.setCountryId(Long.parseLong(arr[3].trim()));

                      jpAirportRepo.save(a);
                  } catch (NumberFormatException e) {
                      System.err.println("⚠ 数値変換エラー: " + line);
                  }
              });

            System.out.println("✅ airports.csv 読み込み完了");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ==========================
    // アプリ起動時処理
    // ==========================
    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 一旦全削除して再ロード（毎回最新化）
        regionAirportRepo.deleteAllInBatch();
        conceptRepo.deleteAllInBatch();
        foodRepo.deleteAllInBatch();
        spotRepo.deleteAllInBatch();
        airportRepo.deleteAllInBatch();
        jpAirportRepo.deleteAllInBatch();
        
        regionRepo.deleteAllInBatch();
        countryRepo.deleteAllInBatch();

        loadCsv();
        System.out.println("🌍 全CSVの読み込み完了 ✅");
    }
}
