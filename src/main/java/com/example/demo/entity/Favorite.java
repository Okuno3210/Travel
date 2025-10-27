package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favorites",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "country_id"})})
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ユーザー
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 国
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;
}
