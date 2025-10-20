package com.example.demo.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
// AllArgsConstructorは初期データ投入のため、IDなしコンストラクタを手動で定義します

@Entity // ★ JPAエンティティとして定義
@Data
@NoArgsConstructor
public class Location {

    @Id // ★ 主キーとして定義
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // IDフィールドを追加

    private String name;
    private double longitude;
    private double latitude;

    // 初期データ投入用のコンストラクタ（IDを除く）
    public Location(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}