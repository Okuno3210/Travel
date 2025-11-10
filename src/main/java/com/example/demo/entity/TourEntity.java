package com.example.demo.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Tour")
public class TourEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;          // CSVのid（DB管理用PK）
    private Long countryId;
    private String countryName;
    private String title;
    private String description;
    private Long basePrice;
    private Long schedule;
    private String imageUrl;
    private String imageUrl2;
    
//    @ManyToOne
//    @JoinColumn(name = "country_id")
//    private Country country;

    
    
    }