package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tourist_spots")
public class TouristSpot {
    @Id
    private Long id;          // CSVのid（DB管理用PK）

    private Long regionId;    // RegionとのJOIN用
    private String name;
    private String description;
}
