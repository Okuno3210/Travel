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
        List<JpAirport> jpAirports = jpAirportRepo.findAll();

        if (regionId != null) {
            // 地域IDから地域情報を取得
            Region region = regionRepo.findById(regionId).orElse(null);

            if (region != null && region.getCountry() != null) {
                // 地域が属する国を取得
                Country country = region.getCountry();

                // その国に属する空港を取得
                airports = airportRepo.findByCountry(country);

                model.addAttribute("selectedRegion", 
                    region.getName() + "（" + country.getName() + "）");
            } else {
                // 地域に国が紐づいていない場合
                airports = airportRepo.findAll();
                model.addAttribute("selectedRegion", "地域未指定");
            }

        } else if (countryId != null) {
            // 国ID指定時
            Country country = countryRepo.findById(countryId).orElse(null);
            if (country != null) {
                airports = airportRepo.findByCountry(country);
                model.addAttribute("selectedRegion", country.getName());
            } else {
                airports = airportRepo.findAll();
                model.addAttribute("selectedRegion", "全空港");
            }

        } else {
            // 両方指定なし
            airports = airportRepo.findAll();
            model.addAttribute("selectedRegion", "全空港");
        }

        model.addAttribute("airports", airports);
        model.addAttribute("japanAirports", jpAirports);

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
     
   //所要時間用
     String flightTime = "不明"; // 初期値
     // 目的地が海外空港 (Airport) の場合、そのRegionから所要時間を取得
     if (destination instanceof Airport airDestination1) {
         // Airport -> Region を辿って flightTime を取得
         if (airDestination1.getRegion() != null) {
             flightTime = airDestination1.getRegion().getFlightTime();
         }
     } //所要時間用ここまで
     
     model.addAttribute("departure", departure);
     model.addAttribute("destination", destination);
     model.addAttribute("date", date);
     model.addAttribute("passenger", passenger);
     model.addAttribute("flightTime", flightTime); //所要時間用

     // 国名判定
     String countryName = getCountryByCode(destinationCode, departureCode);

     // Tour 取得
     TourEntity tour = null;
     if (countryName != null) {
         tour = tourRepo.findByCountryName(countryName).orElse(null);
     }

     // Region 取得（フォールバック用）
     List<Region> regions = regionAirportRepo.findRegionByAirportCode(destinationCode);
     Region region = regions.isEmpty() ? null : regions.get(0);

     // -------------------
     // 料金計算（Tour優先）
     // -------------------
     int totalPrice;
     if (tour != null) {
         totalPrice = tour.getBasePrice().intValue() * passenger;
     } else if (region != null && region.getBudget() != null) {
         try {
             int budget = Integer.parseInt(region.getBudget());
             int half = budget / 2;
             totalPrice = half * passenger;
         } catch (NumberFormatException e) {
             totalPrice = 12 * passenger; // fallback
         }
     } else {
         totalPrice = 12 * passenger; // デフォルト
     }

     model.addAttribute("price", totalPrice);
     model.addAttribute("tour", tour);

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

     // 国名判定
     String countryName = getCountryByCode(destinationCode, departureCode);

     // Tour 取得
     TourEntity tour = null;
     if (countryName != null) {
         tour = tourRepo.findByCountryName(countryName).orElse(null);
     }

     // Region 取得（フォールバック用）
     List<Region> regions = regionAirportRepo.findRegionByAirportCode(destinationCode);
     Region region = regions.isEmpty() ? null : regions.get(0);

     // -------------------
     // 料金計算（Tour優先）
     // -------------------
     int totalPrice;
     if (tour != null) {
         totalPrice = tour.getBasePrice().intValue() * passenger;
     } else if (region != null && region.getBudget() != null) {
         try {
             int budget = Integer.parseInt(region.getBudget());
             int half = budget / 2;
             totalPrice = half * passenger;
         } catch (NumberFormatException e) {
             totalPrice = 12 * passenger; // fallback
         }
     } else {
         totalPrice = 12 * passenger; // デフォルト
     }

     // 予約番号生成
     String bookingNumber = "FL-" + System.currentTimeMillis();

     // FlightBooking 保存
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