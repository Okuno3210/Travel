
package com.example.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Country;
import com.example.demo.entity.Region;
import com.example.demo.entity.TouristSpot;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.TouristSpotRepository;

@Component
public class DataLoader {

    private final RegionRepository regionRepo;
    private final CountryRepository countryRepo;
    private final TouristSpotRepository spotRepo;

    public DataLoader(RegionRepository regionRepo, CountryRepository countryRepo, TouristSpotRepository spotRepo) {
        this.regionRepo = regionRepo;
        this.countryRepo = countryRepo;
        this.spotRepo = spotRepo;
    }

    @Value("classpath:data/region.csv")
    private Resource regionsCsv;

    @Value("classpath:data/country.csv")
    private Resource countriesCsv;

    @Value("classpath:data/tourist_spot.csv")
    private Resource spotsCsv;

    @PostConstruct
    public void init() {
        loadRegions();
        loadCountries();
        loadSpots();

        // ★ CSV増加時はここに loadXxx() を追加
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
                //if (arr.length < 9) return;   // 列数が足りなければスキップ
                
                Region r = new Region();
                r.setId(Long.parseLong(arr[0])); //region.csvは主キーのみにする
                r.setCountry(countryRepo.findById(Long.parseLong(arr[1])).orElse(null));
                r.setName(arr[2]);
                r.setBudget(Integer.parseInt(arr[3]));
                r.setFlightTime(Integer.parseInt(arr[4]));
                r.setTimezone(Integer.parseInt(arr[5]));
                r.setClimate(arr[6]);
                r.setRiskLevel(Integer.parseInt(arr[7]));
                r.setDescription(arr.length > 8 ? arr[8] : ""); // 9列目が無ければ空文字

                regionRepo.save(r);
            });
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadCountries() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader
        		(countriesCsv.getInputStream(), StandardCharsets.UTF_8))) {
            br.lines().skip(1)
            .filter(line -> !line.trim().isEmpty()) // ← 空行を除外
            .forEach(line -> {
                String[] arr = line.split(",");
                if (arr.length < 4) return;   // 列数が足りなければスキップ
                Country c = new Country();
                //c.setId(Long.parseLong(arr[0]));
                c.setName(arr[1]);
                c.setCurrencyRate(arr[2]);
                //c.setDescription(arr[3]);
                c.setDescription(arr.length > 3 ? arr[3] : ""); // 4列目が無ければ空文字
                countryRepo.save(c);                
                
            });
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadSpots() {
        try (BufferedReader br = new BufferedReader
        		(new InputStreamReader(spotsCsv.getInputStream(), StandardCharsets.UTF_8))) {
            br.lines().skip(1)
            .filter(line -> !line.trim().isEmpty()) // ← 空行を除外
            .forEach(line -> {
                String[] arr = line.split(",");
                //if (arr.length < 4) return;   // 列数が足りなければスキップ
                TouristSpot s = new TouristSpot();
                s.setId(Long.parseLong(arr[0])); // id
                s.setRegionId(Long.parseLong(arr[1])); // regionId
                s.setName(arr[2]);                  // Name
                s.setDescription(arr.length > 3 ? arr[3] : ""); // description
                spotRepo.save(s);
            });
        } catch (IOException e) { e.printStackTrace(); }
    }
    //新しいCSVファイル用に private void ファイル名() { try-catch文でメソッドを書き足す
    
}
