
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
@Table(name = "countries")
public class Country {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) //エクリプスで起動する時は有効にする
    private Long id;          // CSVのid（DB管理用PK）

    //private Long regionId;    // RegionとのJOIN用
    
    private String code; // AUS, USA, EGY など

    private String name;
    private String description;
    private String imageUrl; //11/4修正
    
    }
