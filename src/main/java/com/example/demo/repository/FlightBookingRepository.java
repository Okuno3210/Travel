package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.FlightBooking;

public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {

    // 予約番号から1件検索
	Optional<FlightBooking> findByBookingNumber(String bookingNumber);

    // ユーザーIDで予約一覧を取得（お気に入りページで利用）
    List<FlightBooking> findByUserId(Long userId);
    
    //Optional<FlightBooking> findByCode(String code);
}
