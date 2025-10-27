package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Country;
import com.example.demo.entity.Favorite;
import com.example.demo.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
    Optional<Favorite> findByUserAndCountry(User user, Country country);
    void deleteByUserAndCountry(User user, Country country);
}
