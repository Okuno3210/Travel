package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Airport;
import com.example.demo.entity.FlightBooking;
import com.example.demo.entity.Region;
import com.example.demo.repository.AirportRepository;
import com.example.demo.repository.FlightBookingRepository;
import com.example.demo.repository.RegionAirportRepository;
import com.example.demo.repository.RegionRepository;

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

    @Autowired
    private AirportRepository airportRepo;

    @Autowired
    private RegionRepository regionRepo;

    @Autowired
    private RegionAirportRepository regionAirportRepo;

    @Autowired
    private FlightBookingRepository bookingRepo;

    /**
     * ✈ 検索画面の表示
     * - 地域IDが指定された場合、その地域に紐づく空港のみを表示
     * - 指定がない場合は全空港を表示
     */
    @GetMapping("/search")
    public String showFlightSearch(
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String region,
            Model model) {

        List<Airport> airports;

        if (regionId != null) {
            airports = regionAirportRepo.findAirportsByRegionId(regionId);
            Region regionEntity = regionRepo.findById(regionId).orElse(null);
            model.addAttribute("selectedRegion", regionEntity != null ? regionEntity.getName() : "");
        } else {
            airports = airportRepo.findAll();
        }

        model.addAttribute("airports", airports);
        model.addAttribute("country", country);
        model.addAttribute("region", region);
        return "flight/flight-search";
    }

    /**
     * 🔍 検索結果画面
     * ダミー価格で航空券情報を表示
     */
    @GetMapping("/result")
    public String showFlightResult(
            @RequestParam String departureCode,
            @RequestParam String destinationCode,
            @RequestParam String date,
            @RequestParam(required = false) Integer passenger,
            Model model) {

        Airport departure = airportRepo.findByCode(departureCode).orElse(null);
        Airport destination = airportRepo.findByCode(destinationCode).orElse(null);

        if (departure == null || destination == null) {
            model.addAttribute("error", "出発地または目的地の空港情報が見つかりません。");
            return "flight/flight-search";
        }

        model.addAttribute("departure", departure);
        model.addAttribute("destination", destination);
        model.addAttribute("date", date);
        model.addAttribute("passenger", passenger != null ? passenger : 1);
        model.addAttribute("price", 98000); // ダミー価格
        return "flight/flight-result";
    }

    /**
     * ✅ 予約確認画面
     */
    @GetMapping("/confirm")
    public String confirmBooking(
            @RequestParam String departureCode,
            @RequestParam String destinationCode,
            @RequestParam String date,
            @RequestParam Integer passenger,
            @RequestParam Integer price,
            Model model) {

        Airport departure = airportRepo.findByCode(departureCode).orElse(null);
        Airport destination = airportRepo.findByCode(destinationCode).orElse(null);

        model.addAttribute("departure", departure);
        model.addAttribute("destination", destination);
        model.addAttribute("date", date);
        model.addAttribute("passenger", passenger);
        model.addAttribute("price", price);

        return "flight/flight-confirm";
    }

    /**
     * 💾 予約完了処理
     * - DBに予約履歴を保存
     * - 予約番号を発行して完了画面に表示
     */
    @PostMapping("/complete")
    public String completeBooking(
            @RequestParam String departureCode,
            @RequestParam String destinationCode,
            @RequestParam String date,
            @RequestParam Integer passenger,
            @RequestParam Integer price,
            Model model) {

        Airport departure = airportRepo.findByCode(departureCode).orElse(null);
        Airport destination = airportRepo.findByCode(destinationCode).orElse(null);

        if (departure == null || destination == null) {
            model.addAttribute("error", "予約情報に不備があります。");
            return "flight/flight-search";
        }

        // 予約番号を生成
        String bookingNumber = "FL-" + System.currentTimeMillis();

        // DBに保存
        FlightBooking booking = new FlightBooking();
        booking.setDeparture(departure.getName());
        booking.setDestination(destination.getName());
        booking.setDepartureCode(departure.getCode());
        booking.setDestinationCode(destination.getCode());
        booking.setDate(LocalDate.parse(date));
        booking.setPassenger(passenger);
        booking.setPrice(price);
        booking.setBookingNumber(bookingNumber);

        bookingRepo.save(booking);

        // 完了画面へ渡す
        model.addAttribute("bookingNumber", bookingNumber);
        model.addAttribute("departure", departure);
        model.addAttribute("destination", destination);
        model.addAttribute("date", date);
        model.addAttribute("passenger", passenger);
        model.addAttribute("price", price);

        return "flight/flight-complete";
    }
}
