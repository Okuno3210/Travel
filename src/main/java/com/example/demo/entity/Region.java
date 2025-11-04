package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"concepts"})
@Table(name = "region")
public class Region {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // CSVのid、テーブル管理用一意ID自動生成

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;
	
	//@OneToMany(mappedBy="region") private List<Country> countries;
	@OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
	private List<TouristSpot> touristSpots;
	 
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
	   name = "region_concept",
	   joinColumns = @JoinColumn(name = "region_id"),
	   inverseJoinColumns = @JoinColumn(name = "concept_id")
	)
	private List<Concept> concepts;


    private String name;
    private String budget;
    private String flightTime;
    private String timezone;
    private String climate;
    private String riskLevel;
    private String description;
    private String imageUrl;
}