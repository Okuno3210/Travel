package com.example.demo.service; //厚田10/30.11/2修正

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
public class DataLoader implements ApplicationRunner {

	private final CountryRepository countryRepo;
	private final RegionRepository regionRepo;
	private final TouristSpotRepository touristSpotRepo;
	private final FoodRepository foodRepo;
	private final ConceptRepository conceptRepo;
	//CSV増えたら書き足す

	// 2. コンストラクタ（final フィールドの初期化）
	public DataLoader(CountryRepository countryRepo,
			RegionRepository regionRepo, TouristSpotRepository touristSpotRepo,
			FoodRepository foodRepo, ConceptRepository conceptRepo) {
		this.regionRepo = regionRepo;
		this.countryRepo = countryRepo;
		this.touristSpotRepo = touristSpotRepo;
		this.foodRepo = foodRepo;
		this.conceptRepo = conceptRepo;
	}//CSV増えたら書き足す

	@Value("classpath:data/country.csv")
	private Resource countriesCsv;

	@Value("classpath:data/region.csv")
	private Resource regionsCsv;

	@Value("classpath:data/tourist_spot.csv")
	private Resource touristSpotCsv;

	@Value("classpath:data/food.csv")
	private Resource foodsCsv;
	@Value("classpath:data/concept.csv")
	private Resource conceptCsv;
	//CSV増えたら書き足す

	// @PostConstruct 　1027削除、選択肢が二重になる
	private void loadCsv() {
		loadCountries(); //countryを一番に読み込み
		loadRegions(); //2番めにcountryに依存してるregionを読み込む
		loadTouristSpot(); //3番目以降はconcept以外regionに依存 
		loadFoods();
		loadConcepts();
	}
	//★ CSV増加時はここに loadXxx() を追加

	private void loadCountries() {
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(countriesCsv.getInputStream(), StandardCharsets.UTF_8))) {
			br.lines().skip(1)
					.filter(line -> !line.trim().isEmpty()) // ← 空行を除外
					.forEach(line -> {
						String[] arr = line.split(",");
						if (arr.length < 5)
							return; // 列数が足りなければスキップ
						Country c = new Country();
						c.setCode(arr[1]);
						c.setName(arr[2]);
						c.setDescription(arr[3]);
						c.setImageUrl(arr[4]);
						countryRepo.save(c); //カントリー.csvのidエンティティで自動生成して割り振られる  

					});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadRegions() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(regionsCsv.getInputStream(),
				StandardCharsets.UTF_8))) {
			br.lines()
					.skip(1) // ヘッダーをスキップ
					.filter(line -> !line.trim().isEmpty()) // ← 空行を除外
					.forEach(line -> {
						String[] arr = line.split(",");
						if (arr.length < 10)
							return; // 列数が足りなければスキップ
						Region r = new Region();
						//r.setId(Long.parseLong(arr[0])); //region.csvは主キーのみ、自動生成
						Long countryId=Long.parseLong(arr[1]);
						Country country=countryRepo.findById(countryId).orElse(null);
						if(country != null) {r.setCountry(country);}
						r.setName(arr[2]);
						r.setBudget(arr[3]);
						r.setFlightTime(arr[4]);
						r.setTimezone(arr[5]);
						r.setClimate(arr[6]);
						r.setRiskLevel(arr[7]);
						r.setDescription(arr[8]);
						r.setImageUrl(arr[9]);
						regionRepo.save(r);
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadTouristSpot() {
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(touristSpotCsv.getInputStream(), StandardCharsets.UTF_8))) {
			br.lines().skip(1)
					.filter(line -> !line.trim().isEmpty()) // ← 空行を除外
					.forEach(line -> {
						String[] arr = line.split(",");
						if (arr.length > 5) {
							System.out.println("Line length: " + arr.length + " -> " + line);
						}
						TouristSpot s = new TouristSpot();
						s.setId(arr[0]); //string型として読み込む
						Region region = regionRepo.findById(Long.parseLong(arr[1])).orElse(null);
						if (region != null) {
							s.setRegion(region);
						}
						s.setName(arr[2]);
						s.setDescription(arr[3]);
						s.setImageUrl(arr[4]);
						touristSpotRepo.save(s);
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadFoods() {
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
						Region region = regionRepo.findById(Long.parseLong(arr[1].trim())).orElse(null);
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
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(conceptCsv.getInputStream(), StandardCharsets.UTF_8))) {
			br.lines()
					.skip(1) // ヘッダーをスキップ
					.filter(line -> !line.trim().isEmpty()) // ← 空行を除外
					.forEach(line -> {
						String[] arr = line.split(",");
						Concept co = new Concept();
						conceptRepo.save(co);
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//新しいCSVファイル用に private void ファイル名() { try-catch文でメソッドを書き足す

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//H2をfileにした後、選択肢を毎回新規取得するために一旦捨てる　10/30追加
		touristSpotRepo.deleteAll();
		regionRepo.deleteAll();
		countryRepo.deleteAll(); //CSV増えたら書き足す
		foodRepo.deleteAll();
		conceptRepo.deleteAll();

		loadCsv(); //1027追加、deleteしたら読む

	}

}
