package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Airport;
import com.example.demo.entity.Country;
import com.example.demo.entity.FlightBooking;
import com.example.demo.entity.JpAirport;
import com.example.demo.entity.Region;
import com.example.demo.entity.TourEntity;
import com.example.demo.entity.User;
import com.example.demo.repository.AirportRepository;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.FlightBookingRepository;
import com.example.demo.repository.JpAirportRepository;
import com.example.demo.repository.RegionAirportRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.TourRepository;
import com.example.demo.repository.UserRepository;

/**
 * ✈️ FlightController 完全版（2025対応）
 * 機能:
 *  - 航空券検索（地域別絞り込み対応）
 *  - 検索結果表示
 *  - 予約確認画面
 *  - 予約完了処理（DB登録）
 */
@Controller
@RequestMapping("/flight")
public class FlightController {

    @Autowired private JpAirportRepository jpAirportRepo;
    @Autowired private AirportRepository airportRepo;
    @Autowired private TourRepository tourRepo;
    @Autowired private FlightBookingRepository bookingRepo;
    @Autowired private UserRepository userRepository;
    @Autowired private RegionRepository regionRepo;
    @Autowired private RegionAirportRepository regionAirportRepo;
    @Autowired private CountryRepository countryRepo;


    // -----------------------------
    // ✈ 航空券検索画面
    // -----------------------------
    @GetMapping("/search")
    public String showFlightSearch(
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Long countryId,
            Model model) {

        List<Airport> airports;
        List<JpAirport> jpAirport  = jpAirportRepo.findAll();

        if (regionId != null) {
            airports = regionAirportRepo.findAirportsByRegionId(regionId);
            Region region = regionRepo.findById(regionId).orElse(null);
            model.addAttribute("selectedRegion", region != null ? region.getName() : "地域未指定");
        } else if (countryId != null) {
            // ✅ 国名で空港を絞る（これが追加部分）
        	 Country country = countryRepo.findById(countryId).orElse(null);
        	    if (country != null) {
        	        airports = airportRepo.findByCountry(country);
        	        model.addAttribute("selectedRegion", country.getName());
        	    } else {
        	        airports = airportRepo.findAll();
        	    }
        } else {
            airports = airportRepo.findAll();
            model.addAttribute("selectedRegion", "全空港");
        }

        model.addAttribute("airports", airports);
        model.addAttribute("japanAirports", jpAirport);

        return "flight/flight-search";
    }
    // -----------------------------
    // 🔍 検索結果画面
    // -----------------------------
    @GetMapping("/result")
    public String showFlightResult(
            @RequestParam String departureCode,
            @RequestParam String destinationCode,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(defaultValue = "1") Integer passenger,
            @RequestParam(required = false) String bookingNumber,
            Model model) {

        // 出発地取得（日本 or 海外）
        JpAirport jpDeparture = jpAirportRepo.findByCode(departureCode).orElse(null);
        Airport airDeparture = airportRepo.findByCode(departureCode).orElse(null);
        Object departure = (jpDeparture != null) ? jpDeparture : airDeparture;

        // 目的地取得（日本 or 海外）
        JpAirport jpDestination = jpAirportRepo.findByCode(destinationCode).orElse(null);
        Airport airDestination = airportRepo.findByCode(destinationCode).orElse(null);
        Object destination = (jpDestination != null) ? jpDestination : airDestination;

        if (departure == null || destination == null) {
            model.addAttribute("error", "出発地または目的地の空港情報が見つかりません。");
            return "flight/flight-search";
        }

        model.addAttribute("departure", departure);
        model.addAttribute("destination", destination);
        model.addAttribute("date", date);
        model.addAttribute("passenger", passenger);

        FlightBooking booking = null;
        TourEntity tour = null;
        int totalPrice;

        // bookingNumber があれば予約データから国を推測
        if (bookingNumber != null && !bookingNumber.isEmpty()) {
            booking = bookingRepo.findByBookingNumber(bookingNumber).orElse(null);
        }

        // destinationCode から国名を判定
        String countryName = switch (destinationCode) {
        case "LAX", "LAS", "JFK", "HNL","PHX", "IAD", "MIA" -> "USA";
        case "CDG", "NCE" -> "France";
        case "FCO", "FLR", "VCE", "MXP","NAP","CTA","PAS" -> "Italy";
        case "SYD","CNS" -> "Australia";
        case "CAI", "LXR" -> "Egypt";
        case "PEK", "PVG" -> "China";
        case "ICN", "PUS", "CJU" -> "Korea";
        case "ZRH" -> "Switzerland";
        //case "BKK", "HKT" -> "THAI";
        case "IKA" -> "Iran";
        case "DEL" -> "India";
        case "HAN", "DAD", "SGN" -> "Vietnam";
        //case "LED", "SVO" -> "ROSHI";
        default -> null;
        };

        // 出発地が海外の場合（destinationCode が日本）にも対応
        if (countryName == null) {
            countryName = switch (departureCode) {
                case "LAX", "LAS", "JFK", "HNL","PHX", "IAD", "MIA" -> "USA";
                case "CDG", "NCE" -> "France";
                case "FCO", "FLR", "VCE", "MXP","NAP","CTA","PAS" -> "Italy";
                case "SYD","CNS" -> "Australia";
                case "CAI", "LXR" -> "Egypt";
                case "PEK", "PVG" -> "China";
                case "ICN", "PUS", "CJU" -> "Korea";
                case "ZRH" -> "Switzerland";
                //case "BKK", "HKT" -> "THAI";
                case "IKA" -> "Iran";
                case "DEL" -> "India";
                case "HAN", "DAD", "SGN" -> "Vietnam";
                //case "LED", "SVO" -> "ROSHI";
                default -> null;
            };
        }

        // Tour 取得
        if (countryName != null) {
            tour = tourRepo.findByCountryName(countryName).orElse(null);
        }

        List<Region> regions = regionAirportRepo.findRegionByAirportCode(destinationCode);
        Region region = regions.isEmpty() ? null : regions.get(0);  // とりあえず最初のを採用


        if (region != null && region.getBudget() != null) {
            try {
                int budget = Integer.parseInt(region.getBudget()); // budgetはCSVで数値文字列
                int half = budget / 2;
                totalPrice = half * passenger;
            } catch (NumberFormatException e) {
                totalPrice = 12 * passenger; // 数字に変換できないときの fallback
            }
        } else if (tour != null) {
            totalPrice = tour.getBasePrice().intValue() * passenger;
        } else {
            totalPrice = 12 * passenger;
        }


        model.addAttribute("price", totalPrice);
        model.addAttribute("tour", tour);
        model.addAttribute("booking", booking);

        return "flight/flight-result";
    }


    // -----------------------------
    // ✅ 予約確認画面
    // -----------------------------
    @GetMapping("/confirm")
    public String confirmBooking(
            @RequestParam String departureCode,
            @RequestParam String destinationCode,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(defaultValue = "1") Integer passenger,
            @RequestParam Integer price,
            Model model) {

        // 出発地取得（日本 or 海外）
        JpAirport jpDeparture = jpAirportRepo.findByCode(departureCode).orElse(null);
        Airport airDeparture = airportRepo.findByCode(departureCode).orElse(null);
        Object departure = (jpDeparture != null) ? jpDeparture : airDeparture;

        // 目的地取得（日本 or 海外）
        JpAirport jpDestination = jpAirportRepo.findByCode(destinationCode).orElse(null);
        Airport airDestination = airportRepo.findByCode(destinationCode).orElse(null);
        Object destination = (jpDestination != null) ? jpDestination : airDestination;

        if (departure == null || destination == null) {
            model.addAttribute("error", "予約情報に不備があります。");
            return "flight/flight-search";
        }

        // Model に情報を追加
        model.addAttribute("departure", departure);
        model.addAttribute("destination", destination);
        model.addAttribute("date", date);
        model.addAttribute("passenger", passenger);
        model.addAttribute("price", price);

        return "flight/flight-confirm";
    }

    // -----------------------------
    // 💾 予約完了処理
    // -----------------------------
    @PostMapping("/complete")
    public String completeBooking(
            @RequestParam String departureCode,
            @RequestParam String destinationCode,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(defaultValue = "1") Integer passenger,
            Model model,
            Principal principal) {

        // 出発地取得（日本 or 海外）
        JpAirport jpDeparture = jpAirportRepo.findByCode(departureCode).orElse(null);
        Airport airDeparture = airportRepo.findByCode(departureCode).orElse(null);
        Object departure = (jpDeparture != null) ? jpDeparture : airDeparture;

        // 目的地取得（日本 or 海外）
        JpAirport jpDestination = jpAirportRepo.findByCode(destinationCode).orElse(null);
        Airport airDestination = airportRepo.findByCode(destinationCode).orElse(null);
        Object destination = (jpDestination != null) ? jpDestination : airDestination;

        if (departure == null || destination == null) {
            model.addAttribute("error", "予約情報に不備があります。");
            return "flight/flight-search";
        }

        // ログインユーザー取得
        if (principal == null) {
            model.addAttribute("error", "ログインしてから予約を完了してください。");
            return "redirect:/login";
        }
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("ユーザーが存在しません。"));

        // countryName を destinationCode から判定
        String countryName = switch (destinationCode) {
        case "LAX", "LAS", "JFK", "HNL","PHX", "IAD", "MIA" -> "USA";
        case "CDG", "NCE" -> "France";
        case "FCO", "FLR", "VCE", "MXP","NAP","CTA","PAS" -> "Italy";
        case "SYD","CNS" -> "Australia";
        case "CAI", "LXR" -> "Egypt";
        case "PEK", "PVG" -> "China";
        case "ICN", "PUS", "CJU" -> "Korea";
        case "ZRH" -> "Switzerland";
        //case "BKK", "HKT" -> "THAI";
        case "IKA" -> "Iran";
        case "DEL" -> "India";
        case "HAN", "DAD", "SGN" -> "Vietnam";
        //case "LED", "SVO" -> "ROSHI";
        default -> null;
        };

        // 出発地が海外の場合（destinationCode が日本）にも対応
        if (countryName == null) {
            countryName = switch (departureCode) {
            case "LAX", "LAS", "JFK", "HNL","PHX", "IAD", "MIA" -> "USA";
            case "CDG", "NCE" -> "France";
            case "FCO", "FLR", "VCE", "MXP","NAP","CTA","PAS" -> "Italy";
            case "SYD","CNS" -> "Australia";
            case "CAI", "LXR" -> "Egypt";
            case "PEK", "PVG" -> "China";
            case "ICN", "PUS", "CJU" -> "Korea";
            case "ZRH" -> "Switzerland";
            //case "BKK", "HKT" -> "THAI";
            case "IKA" -> "Iran";
            case "DEL" -> "India";
            case "HAN", "DAD", "SGN" -> "Vietnam";
            //case "LED", "SVO" -> "ROSHI";
            default -> null;
            };
        }

        // Tour 取得
        TourEntity tour = null;
        int totalPrice;
        if (countryName != null) {
            tour = tourRepo.findByCountryName(countryName).orElse(null);
        }

        List<Region> regions = regionAirportRepo.findRegionByAirportCode(destinationCode);
        Region region = regions.isEmpty() ? null : regions.get(0);  // とりあえず最初のを採用


        if (region != null && region.getBudget() != null) {
            try {
                int budget = Integer.parseInt(region.getBudget()); // budgetはCSVで数値文字列
                int half = budget / 2;
                totalPrice = half * passenger;
            } catch (NumberFormatException e) {
                totalPrice = 12 * passenger; // 数字に変換できないときの fallback
            }
        } else if (tour != null) {
            totalPrice = tour.getBasePrice().intValue() * passenger;
        } else {
            totalPrice = 12 * passenger;
        }


        // 予約番号生成
        String bookingNumber = "FL-" + System.currentTimeMillis();

        // FlightBooking 作成・保存
        FlightBooking booking = new FlightBooking();
        booking.setDeparture((departure instanceof JpAirport jp ? jp.getName() : ((Airport) departure).getName()));
        booking.setDestination((destination instanceof JpAirport jp ? jp.getName() : ((Airport) destination).getName()));
        booking.setDepartureCode(departureCode);
        booking.setDestinationCode(destinationCode);
        booking.setDate(date);
        booking.setPassenger(passenger);
        booking.setPrice(totalPrice);
        booking.setBookingNumber(bookingNumber);
        booking.setUser(user);
        bookingRepo.save(booking);

        // Model に情報を追加
        model.addAttribute("bookingNumber", bookingNumber);
        model.addAttribute("departure", departure);
        model.addAttribute("destination", destination);
        model.addAttribute("date", date);
        model.addAttribute("passenger", passenger);
        model.addAttribute("price", totalPrice);
        model.addAttribute("tour", tour);
        model.addAttribute("booking", booking);

        return "flight/flight-complete";
    }

    // -----------------------------
    // 🛠 ヘルパー: 国名判定
    // -----------------------------
    private String getCountryByCode(String destinationCode, String departureCode) {
        Map<String, String> map = Map.ofEntries(
            Map.entry("LAX", "USA"), Map.entry("LAS", "USA"), Map.entry("JFK", "USA"), Map.entry("HNL", "USA"),
            Map.entry("CDG", "France"), Map.entry("FCO", "Italy"), Map.entry("SYD", "Australia"),
            Map.entry("CAI", "Egypt"), Map.entry("BJS", "China"), Map.entry("PEK", "China"), Map.entry("ICN", "Korea")
        );

        String country = map.get(destinationCode);
        if (country == null) country = map.get(departureCode);
        return country;
    }
}