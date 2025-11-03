package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
@Table(name = "concept") // テーブル名は任意で変更可
public class Concept {
    @Id
    @GeneratedValue
    private Long id;          // DB管理用PK

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;    // RegionとのJOIN

    private String name;
    
    public Long getId() {return id;}
    public void setId(Long id) {this.id=id;}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

