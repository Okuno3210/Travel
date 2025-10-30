package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "region")
public class Region {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // CSVのid、テーブル管理用一意ID自動生成

	@ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
	//@OneToMany(mappedBy="region") private List<Country> countries;
	@OneToMany(mappedBy="region")
	private List<TouristSpot> touristSpots;

    private String name;
    private String budget;
    private String flightTime;
    private String timezone;
    private String climate;
    private String riskLevel;
    private String description;
    private String imageUrl;
}