package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {

    // 地域ごとの食品を取得（RegionをJOIN FETCH）
    @Query("""
        SELECT f
        FROM Food f
        LEFT JOIN FETCH f.region r
        WHERE (:regionId IS NULL OR r.id = :regionId)
        AND (:foodName IS NULL OR f.name LIKE %:foodName%)
    """)
    List<Food> searchFoods(
        @Param("regionId") Long regionId,
        @Param("foodName") String foodName
    );

    // 全件取得（Regionをまとめてフェッチ）
    @Query("SELECT f FROM Food f LEFT JOIN FETCH f.region r")
    List<Food> findAllWithRegion();
}
