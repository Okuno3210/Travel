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
@Table(name = "foods") // テーブル名は任意で変更可
public class Food {

    @Id
    @GeneratedValue
    private Long id;          // DB管理用PK

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;    // RegionとのJOIN

    private String name;
    private String description;
    private String imgUrl;
}
