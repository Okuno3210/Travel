package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Airport;
import com.example.demo.entity.FlightBooking;
import com.example.demo.entity.JpAirport;
import com.example.demo.entity.TourEntity;
import com.example.demo.entity.User;
import com.example.demo.repository.AirportRepository;
import com.example.demo.repository.FlightBookingRepository;
import com.example.demo.repository.JpAirportRepository;
import com.example.demo.repository.TourRepository;
import com.example.demo.repository.UserRepository;

/**
 * ✈️ ツアー一覧から遷移用FlightController 完全版（2025対応）
 * 機能:
 *  - 航空券検索（地域別絞り込み対応）
 *  - 検索結果表示
 *  - 予約確認画面
 *  - 予約完了処理（DB登録）
 */
@Controller
@RequestMapping("/tour/flight")
public class TourFlightController {

    @Autowired private JpAirportRepository jpAirportRepo;
    @Autowired private AirportRepository airportRepo;
    @Autowired private TourRepository tourRepo;
    @Autowired private FlightBookingRepository bookingRepo;
    @Autowired private UserRepository userRepository;
    
    public TourFlightController(
            JpAirportRepository jpAirportRepo,
            AirportRepository airportRepo,
            TourRepository tourRepo,
            FlightBookingRepository bookingRepo,
            UserRepository userRepository) {
        this.jpAirportRepo = jpAirportRepo;
        this.airportRepo = airportRepo;
        this.tourRepo = tourRepo;
        this.bookingRepo = bookingRepo;
        this.userRepository = userRepository;
    }

    // -----------------------------
    // ✈ 航空券検索画面
    // -----------------------------
    @GetMapping("/search")
    public String showFlightSearch(
            @RequestParam(required = false) Long countryId,
            Model model) {

        List<Airport> airports; //日本の空港は常に7件取得
        List<JpAirport> jpAirport  = jpAirportRepo.findAll();

        if (countryId != null) {
        	airports = airportRepo.findByCountryId(countryId);
        	Optional<TourEntity> tour = tourRepo.findById(countryId);
            model.addAttribute("tour", tour.orElse(null));
        } else {airports = airportRepo.findAll();}
        model.addAttribute("airports", airports);
        model.addAttribute("japanAirports", jpAirport);
        return "flight/tour-flight-search";
     }
            
    // -----------------------------
    // 🔍 検索結果画面
    // -----------------------------
    @GetMapping("/result")
    public String showFlightResult(
    		@RequestParam(required = false) Long countryId, 
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
            return "flight/tour-flight-search";
        }

        model.addAttribute("departure", departure);
        model.addAttribute("destination", destination);
        model.addAttribute("date", date);
        model.addAttribute("passenger", passenger);
        
        //価格計算
        FlightBooking booking = null;
        TourEntity tour = null;
        int totalPrice=12*passenger;
        
     // ★★★ countryId があればツアーの基本価格を使う ★★★
        if (countryId != null) {
            tour = tourRepo.findById(countryId).orElse(null);
            if (tour != null) {
                totalPrice = tour.getBasePrice().intValue() * passenger;
            }
        }
        
        // bookingNumber があれば予約データから国を推測
        if (bookingNumber != null && !bookingNumber.isEmpty()) {
            booking = bookingRepo.findByBookingNumber(bookingNumber).orElse(null);
        }
        model.addAttribute("price",totalPrice);
        model.addAttribute("tour",tour);
        model.addAttribute("booking",booking);
        return "flight/tour-flight-result";}
        
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
            return "flight/tour-flight-search";
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
            @RequestParam Integer price,
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
            return "flight/tour-flight-search";
        }

        // ログインユーザー取得
        if (principal == null) {
            model.addAttribute("error", "ログインしてから予約を完了してください。");
            return "redirect:/login";
        }
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("ユーザーが存在しません。"));

        // Tour 取得
        TourEntity tour = null;
        int totalPrice = price;
        
        Optional<Airport>destAirport=airportRepo.findByCode(destinationCode);
        if(destAirport.isPresent() && destAirport.get().getCountry() != null)
        { 
            // Airport.country.id を使って TourEntity を検索
            Long countryId = destAirport.get().getCountry().getId();
            tour = tourRepo.findById(countryId).orElse(null);
        }
        
        if(tour != null) {
        	totalPrice=tour.getBasePrice().intValue()*passenger;
        }else {
        	totalPrice=12*passenger;
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
    }