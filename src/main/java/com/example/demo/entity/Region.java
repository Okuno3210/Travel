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
	//@ManyToOne  private TouristSpot touristSpot;
	@ManyToOne private Concept concept; //←中間テーブルを足したら不要になるかも

	@OneToMany(mappedBy="region")
	private List<TouristSpot> touristSpots;
	@OneToMany(mappedBy="region")
	private List<Food> food;

    private String name;
    private String budget;
    private String flightTime;
    private String timezone;
    private String climate;
    private String riskLevel;
    private String description;
    private String imageUrl;
    
    public Long getId() {return id;}
    public void setId(Long id) {this.id=id;}
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String getFlightTime() {
		return flightTime;
	}
	public void setFlightTime(String flightTime) {
		this.flightTime = flightTime;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public String getClimate() {
		return climate;
	}
	public void setClimate(String climate) {
		this.climate = climate;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public List<Food> getFood() {
		return food;
	}
	public void setFood(List<Food> food) {
		this.food = food;
	}

	public Country getCountry() {return country;} //以下検索用
	public void setCountry(Country country) {
		this.country = country;}
	public List<TouristSpot> getTouristSpots() {return touristSpots;}
	public void setTouristSpots(List<TouristSpot> touristSpots) {
		this.touristSpots = touristSpots;}
	public Concept getConcept() {return concept;} //←中間テーブルを足したら不要になるかも
	public void setConcept(Concept concept) {
		this.concept = concept;}
}