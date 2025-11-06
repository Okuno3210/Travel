package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "bookings")
public class FlightBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 自動採番

    // 出発地
    private String departure;

    // 到着地
    private String destination;

    // 出発地コード（例：HND）
    private String departureCode;

    // 到着地コード（例：LAX）
    private String destinationCode;

    // 出発日
    private LocalDate date;

    // 予約人数
    private Integer passenger;

    // 価格
    private Integer price;

    // 予約番号（例：FL-1730792012345）
    private String bookingNumber;
    
    // ユーザー
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
