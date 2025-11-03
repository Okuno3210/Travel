package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    //@GeneratedValue//IDはstringで読み込むため自動生成やめた
    private String id;          // CSVのid（DB管理用PK）
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;    // RegionとのJOIN用
    private String name;
    private String description;
    private String imageUrl;
    
    public String getId() {return id;}
    public void setId(String id) {this.id=id;}
    
    public Region getRegion() {return region;}
    public void setRegion(Region region) {this.region=region;}
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
}
