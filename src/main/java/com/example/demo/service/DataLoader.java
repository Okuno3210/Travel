
package com.example.demo.service; //厚田10/27修正

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Country;
import com.example.demo.entity.Region;
import com.example.demo.entity.TouristSpot;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.TouristSpotRepository;

@Component
public class DataLoader implements ApplicationRunner{// 1027削除　CommandLineRunner {
   
	private final CountryRepository countryRepo;
	private final RegionRepository regionRepo;
    private final TouristSpotRepository spotRepo;
    //CSV増えたら書き足す
    
    // 2. コンストラクタ（final フィールドの初期化）
    public DataLoader(CountryRepository countryRepo, 
    RegionRepository regionRepo, TouristSpotRepository spotRepo) {
        this.regionRepo = regionRepo;
        this.countryRepo = countryRepo;
        this.spotRepo = spotRepo;
    }//CSV増えたら書き足す

    @Value("classpath:data/country.csv")
    private Resource countriesCsv;
    
    @Value("classpath:data/region.csv")
    private Resource regionsCsv;
    
    @Value("classpath:data/tourist_spot.csv")
    private Resource spotsCsv;
    //CSV増えたら書き足す

    // @PostConstruct 　1027削除
    // public void init() {
    private void loadCsv() { //1027追加
    	loadCountries(); //countryを一番に読み込み
    	loadRegions();   //2番めにcountryに依存してるregionを読み込む
        loadSpots();     //3番目以降はconcept以外regionに依存        
    }
    
//★ CSV増加時はここに loadXxx() を追加

    private void loadCountries() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader
        		(countriesCsv.getInputStream(), StandardCharsets.UTF_8))) {
            br.lines().skip(1)
            .filter(line -> !line.trim().isEmpty()) // ← 空行を除外
            .forEach(line -> {
                String[] arr = line.split(",");
                if (arr.length < 4) return;   // 列数が足りなければスキップ
                Country c = new Country();
                c.setName(arr[1]);
                c.setCurrencyRate(arr[2]);
                c.setDescription(arr.length > 3 ? arr[3] : ""); // 4列目が無ければ空文字
                countryRepo.save(c); //カントリー.csvのidに限りエンティティで自動生成して割り振られる  
                
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
                //if (arr.length < 9) return;   // 列数が足りなければスキップ
                Region r = new Region();
                //r.setId(Long.parseLong(arr[0])); //region.csvは主キーのみ、自動生成
                r.setCountry(countryRepo.findById(Long.parseLong(arr[1])).orElse(null));
                r.setName(arr[2]);
                r.setBudget(Integer.parseInt(arr[3]));
                r.setFlightTime(Integer.parseInt(arr[4]));
                r.setTimezone(Integer.parseInt(arr[5]));
                r.setClimate(arr[6]);
                r.setRiskLevel(Integer.parseInt(arr[7]));
                r.setDescription(arr.length > 8 ? arr[8] : ""); // 9列目が無ければ空文字
                
                Country country = countryRepo.findById(Long.parseLong(arr[1])).orElse(null);
                if(country != null) {
                	r.setCountry(country);}
                	regionRepo.save(r);
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
                TouristSpot s = new TouristSpot();
                //s.setId(Long.parseLong(arr[0]));
                //s.setRegionId(Long.parseLong(arr[1]));
                s.setName(arr[2]);
                s.setDescription(arr.length > 3 ? arr[3] : "");
                Region region = regionRepo.findById(Long.parseLong(arr[1])).orElse(null);
                if(region != null) {
                	s.setRegion(region);
                }
                spotRepo.save(s);
            });
        } catch (IOException e) { e.printStackTrace(); }
    }
    //新しいCSVファイル用に private void ファイル名() { try-catch文でメソッドを書き足す
    
    @Override
    public void run(ApplicationArguments args) throws Exception{ //1027追加
    	loadCsv(); //1027追加
    //1027削除　public void run(String[] args)throws Exception{
    	        // loadRegions(); loadSpots();
    	
    	//DBに保存できてるか確認ログ
    	//regionRepo.findAllWithSpots()
    	//.forEach(r -> System.out.println(r.getName()
    			//+":"+ r.getTouristSpots().size()));
    }

    
}
