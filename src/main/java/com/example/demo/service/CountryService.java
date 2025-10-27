package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Country;
import com.example.demo.repository.CountryRepository;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    // ID(Long) で取得
    public Country findById(Long id) {
        return countryRepository.findById(id).orElse(null);
    }

    // コード(String)で取得
    public Country findByCode(String code) {
        return countryRepository.findByCode(code).orElse(null); // Optional から null に変換
    }

}
