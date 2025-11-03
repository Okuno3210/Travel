package com.example.demo.controller; //1030,11/2厚田修正

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Region;
import com.example.demo.entity.TouristSpot;
import com.example.demo.repository.ConceptRepository;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.TouristSpotRepository;

@RestController
@RequestMapping("/api/regions")
public class RegionApiController {

	private final CountryRepository countryRepo;
	private final RegionRepository regionRepo;
	private final TouristSpotRepository touristSpotRepo;
	private final ConceptRepository conceptRepo;

	public RegionApiController(RegionRepository regionRepo,
			TouristSpotRepository touristSpotRepo, CountryRepository countryRepo,
			ConceptRepository conceptRepo) {
		this.regionRepo = regionRepo;
		this.touristSpotRepo = touristSpotRepo;
		this.countryRepo = countryRepo;
		this.conceptRepo = conceptRepo;
	}

	// ▼ region の選択肢取得（CSVから読み込んだデータを返す）
	@GetMapping("/options")
	public List<Map<String, Object>> getRegionOptions() {
		List<Map<String, Object>> result = new ArrayList<>();
		for (Region r : regionRepo.findAll()) {
			result.add(Map.of("id", r.getId(), "name", r.getName()));
		}
		return result;
	}

	// ▼ 検索機能（region・spot・時間・予算を条件に検索）
	@GetMapping("/search")
	public List<Map<String, Object>> searchRegions(
			@RequestParam(required = false) Long countryId,
			@RequestParam(required = false) String regionName,
			@RequestParam(required = false) Long regionId,
			@RequestParam(required = false) String touristSpotId,
			@RequestParam(required = false) String flightTime, //10/30 Stringに変更
			@RequestParam(required = false) Integer budget,
			@RequestParam(required = false) String concept)
	{
		List<Region> regions = regionRepo.searchRegions(countryId, 
				regionName, regionId, touristSpotId,
				flightTime, budget,concept);

		return regions.stream()
				.map(r -> Map.of(
		"countryName", r.getCountry() != null ? r.getCountry().getName() : "",
		"name", r.getName(),
		"spots", r.getTouristSpots() != null ? r.getTouristSpots().stream()
				.map(TouristSpot::getName).collect(Collectors.toList())
				: List.of(),
		"flightTime", r.getFlightTime(),
		"budget", r.getBudget(),
		"description",r.getDescription(),
		"concept", r.getConcept() != null ? r.getConcept().getName() : ""
				))
				.collect(Collectors.toList());
	}

	@GetMapping("/flight-times") // 渡航時間リスト
	public List<String> getflightTimes() {
		return regionRepo.findAll().stream() //逐次処理用にデータをバラす
				.map(Region::getFlightTime)
				.distinct() //重複防止
				.sorted() //昇り順で並べる
				.collect(Collectors.toList()); //バラしたデータをListにまたまとめる
	}

	@GetMapping("/budgets") // 予算リスト
	public List<String> getBudgets() {
		return regionRepo.findAll().stream()
				.map(Region::getBudget)
				.distinct() //重複防止
				.sorted() //昇り順で並べる
				.collect(Collectors.toList());
	}

}
