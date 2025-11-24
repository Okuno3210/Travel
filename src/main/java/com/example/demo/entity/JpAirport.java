package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "japan-airports")
@Getter
@Setter
public class JpAirport {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) //エクリプスで起動する時は有効にする
    private Long id;

    private String name;
    private String code;
    private Long countryId;
}
