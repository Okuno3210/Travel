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
@Table(name = "region")
public class Region {
	@Id
    private Long id;        // CSVのid、テーブル管理用一意ID

    //private Long regionId;  // CSVのregion_id、検索・JOIN用の共通キー

	@ManyToOne //(optional = true)  //以下３行、Region は 多対一（ManyToOne） で Country に紐づく
    @JoinColumn(name = "country_id")
    private Country country;

    private String name;
    private Integer budget;
    private Integer flightTime;
    private Integer timezone;
    private String climate;
    private Integer riskLevel;
    private String description;
}