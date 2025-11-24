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

import com.example.demo.entity.TourEntity;
import com.example.demo.repository.TourRepository;



//@Component //エクリプスで起動する時は有効にする
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
	                        //to.setId(Long.parseLong(arr[0])); //エクリプスで起動する時は無効にする
	                        to.setCountryId(parseLongOrNull(arr[1]));
	                        to.setCountryName(arr[2]);
	                        to.setTitle(arr[3]);
	                        to.setDescription(arr[4]);
	                        to.setBasePrice(parseLongOrNull(arr[5]));
	                        to.setSchedule(parseLongOrNull(arr[6]));
	                        to.setImageUrl(arr[7]);
	                        to.setImageUrl2(arr[8]);
	                        
	                        tourRepo.save(to);
	                        
	                    });System.out.println("✅ tour.csv 読み込み完了");
	                    
	        } catch (IOException e) { e.printStackTrace(); }}
	 
	// null用
	 private Long parseLongOrNull(String csvValue) {
	     if (csvValue == null || csvValue.trim().isEmpty() || csvValue.equalsIgnoreCase("null")) {
	         // "null"という文字列、または空、nullを検出した場合
	         return null; 
	     }
	     try {
	         return Long.parseLong(csvValue.trim());
	         
	         
	         
	     } catch (NumberFormatException e) {
	         System.err.println("不正な数値データが見つかりました: " + csvValue);
	         // 不正な値は null として処理を続行
	         return null; 
	     }
	 }
	 
	 
	// アプリ起動時処理
	 @Transactional
	    @Override
	    public void run(ApplicationArguments args) throws Exception {
	        // 一旦全削除して再ロード（毎回最新化）
		 tourRepo.deleteAllInBatch();
		 loadCsv();}


}
