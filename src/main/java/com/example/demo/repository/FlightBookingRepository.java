package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.FlightBooking;

public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {
    // 必要に応じてカスタムクエリも追加可能
    FlightBooking findByBookingNumber(String bookingNumber);
}
