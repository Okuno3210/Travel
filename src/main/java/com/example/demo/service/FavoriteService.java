package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Country;
import com.example.demo.entity.Favorite;
import com.example.demo.entity.User;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.FavoriteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final CountryRepository countryRepository;

    // お気に入り登録
    @Transactional
    public void addFavorite(User user, Long countryId) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new IllegalArgumentException("Country not found"));

        Optional<Favorite> existing = favoriteRepository.findByUserAndCountry(user, country);
        if (existing.isEmpty()) {
            favoriteRepository.save(new Favorite(null, user, country));
        }
    }

    // お気に入り解除
    @Transactional
    public void removeFavorite(User user, Long countryId) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new IllegalArgumentException("Country not found"));
        favoriteRepository.deleteByUserAndCountry(user, country);
    }

    // ユーザーのお気に入り一覧
    @Transactional(readOnly = true)
    public List<Favorite> getFavorites(User user) {
        return favoriteRepository.findByUser(user);
    }
}
