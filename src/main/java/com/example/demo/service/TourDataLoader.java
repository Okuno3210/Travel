package com.example.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.example.demo.entity.TourEntity;
import com.example.demo.repository.TourRepository;



@Component
public class TourDataLoader implements ApplicationRunner {
	private final TourRepository tourRepo;
	public TourDataLoader(
			TourRepository tourRepo) {
			this.tourRepo = tourRepo;}
	 @Value("classpath:data/tour.csv")
	    private Resource tourCsv;
	 private void loadCsv() {
	        loadTour();}
	 
	 private void loadTour() {
	        try (BufferedReader br = new BufferedReader(new InputStreamReader(
	                tourCsv.getInputStream(), StandardCharsets.UTF_8))) {

	            br.lines().skip(1).filter(line -> !line.trim().isEmpty())
	                    .forEach(line -> {
	                        String[] arr = line.split(",");
	                        // if (arr.length < 7) return;

	                        TourEntity to = new TourEntity();
	                        //to.setCountryId(Long.parseLong(arr[1]));
	                        to.setCountryName(arr[2]);
	                        to.setTitle(arr[3]);
	                        to.setDescription(arr[4]);
	                        to.setBasePrice(Long.parseLong(arr[5]));
	                        to.setSchedule(Long.parseLong(arr[6]));
	                        to.setImageUrl(arr[7]);
	                        to.setImageUrl2(arr[8]);
	                        tourRepo.save(to);
	                        
	                    });
	        } catch (IOException e) { e.printStackTrace(); }}
	 
	// アプリ起動時処理
	 @Transactional
	    @Override
	    public void run(ApplicationArguments args) throws Exception {
	        // 一旦全削除して再ロード（毎回最新化）
		 tourRepo.deleteAllInBatch();
		 loadCsv();}


}
