

package com.example.demo.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Concept;
import com.example.demo.entity.Country;
import com.example.demo.entity.Region;

@Service

@Repository
public class SelectService {
	
	private List<Country> countryData = new ArrayList<>();
    private List<Region> regionData = new ArrayList<>();
    private List<Concept> conceptData = new ArrayList<>();
   
    
    

    // ✅ アプリ起動時にCSVを読み込む、Countryを先に読み込む処理
    @PostConstruct
    public void inData() {
        loadCountryData();
        loadConceptData();
        loadRegionData();
        loadRegionConceptData();
    }
    
    
    public void loadRegionData() {
        try {
            // resources/data/region.csv を取得
            InputStream inputStream = getClass().getResourceAsStream("/data/region.csv");
            if (inputStream == null) {
                throw new IllegalStateException("CSVファイルが見つかりません: /data/region.csv");
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                String line;
                boolean isHeader = true;
            
                while ((line = reader.readLine()) != null) {
                    if (isHeader) { // 1行目はヘッダーなのでスキップ
                        isHeader = false;
                        continue;
                    }

               
                    // カンマ区切りで分割（カンマが文字列中にない前提）
                    String[] cols = line.split(",", -1); // -1で空文字も保持

                    if (cols.length < 10) {
                        System.err.println("⚠️ 無効なCSV行: " + line);
                        continue;
                    }

                    Region region = new Region();
                    region.setId(Long.parseLong(cols[0]));
                    
                    Long countryId = Long.parseLong(cols[1]);

                    // countryData の中から一致する Country を検索
                    Country country = countryData.stream()
                            .filter(c -> c.getId().equals(countryId))
                            .findFirst()
                            .orElse(null);

                    if (country == null) {
                        System.err.println("⚠️ Countryが見つかりません: id=" + countryId);
                        continue; // 見つからなければスキップ
                    }

                    region.setCountry(country);
                          
                    region.setName(cols[2]);
                    region.setBudget(getRegionBudget(cols[3]));
                    region.setFlightTime(getFlightTimeCategory(cols[4]));
                    region.setTimezone(cols[5]);
                    region.setClimate(cols[6]);
                    region.setRiskLevel(cols[7]);
                    region.setDescription(cols[8]);
                    region.setImageUrl(cols[9]);

                    region.setConcepts(new ArrayList<>());
                    
                    regionData.add(region);
                }
            }

            System.out.println("✅ CSVから " + regionData.size() + " 件の地域データを読み込みました。");

        } catch (Exception e) {
            System.err.println("❌ CSV読み込み中にエラー: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void loadConceptData() {
        try (InputStream inputStream = getClass().getResourceAsStream("/data/concept.csv")) {
            if (inputStream == null) {
                throw new IllegalStateException("CSVファイルが見つかりません: /data/concept.csv");
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                String line;
                boolean isHeader = true;

                while ((line = reader.readLine()) != null) {
                    if (isHeader) {
                        isHeader = false;
                        continue;
                    }

                    String[] cols = line.split(",", -1);
                    if (cols.length < 2) {
                        System.err.println("⚠️ 無効なCSV行: " + line);
                        continue;
                    }

                    Concept concept = new Concept();
                    concept.setId(Long.parseLong(cols[0]));
                    concept.setName(cols[1]);
                    conceptData.add(concept);
                }

                System.out.println("✅ CSVから " + conceptData.size() + " 件のコンセプトデータを読み込みました。");
            }
        } catch (Exception e) {
            System.err.println("❌ concept.csv読み込み中にエラー: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void loadCountryData(){
        try {
            // resources/data/region.csv を取得
            InputStream inputStream = getClass().getResourceAsStream("/data/country.csv");
            if (inputStream == null) {
                throw new IllegalStateException("CSVファイルが見つかりません: /data/country.csv");
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                String line;
                boolean isHeader = true;

                while ((line = reader.readLine()) != null) {
                    if (isHeader) { // 1行目はヘッダーなのでスキップ
                        isHeader = false;
                        continue;
                    }

                    // カンマ区切りで分割（カンマが文字列中にない前提）
                    String[] cols = line.split(",", -1); // -1で空文字も保持

                    if (cols.length < 5) {
                        System.err.println("⚠️ 無効なCSV行: " + line);
                        continue;
                    }

                   Country country = new Country();
                   country.setId(Long.parseLong(cols[0]));
                   country.setCode(cols[1]);
                   country.setName(cols[2]);
                   country.setDescription(cols[3]);
                   

                    countryData.add(country);
                }
                
            }

        System.out.println("✅ CSVから " + countryData.size() + " 件の地域データを読み込みました。");

    } catch (Exception e) {
        System.err.println("❌ CSV読み込み中にエラー: " + e.getMessage());
        e.printStackTrace();
    }
    }
    
    public void loadRegionConceptData() {
        try (InputStream inputStream = getClass().getResourceAsStream("/data/region_concept.csv")) {
            if (inputStream == null) throw new IllegalStateException("region_concept.csv が見つかりません");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                reader.lines().skip(1).forEach(line -> {
                    String[] cols = line.split(",", -1);
                    if (cols.length < 3) return;

                    Long regionId = Long.parseLong(cols[1]);
                    Long conceptId = Long.parseLong(cols[2]);

                    Region region = regionData.stream()
                            .filter(r -> r.getId().equals(regionId))
                            .findFirst()
                            .orElse(null);
                    Concept concept = conceptData.stream()
                            .filter(c -> c.getId().equals(conceptId))
                            .findFirst()
                            .orElse(null);

                    if (region != null && concept != null) {
                        region.getConcepts().add(concept);
                    }
                });
                System.out.println("✅ region_concept.csv から Region↔Concept 紐付け完了");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // ✅ 全件取得
  public List<Region> getAllRegions() {
    	
        return regionData;
    }
  
  public List<Country> getAllCountries() {
	    return countryData;
	}
  
  public List<Concept> getAllConcepts() {
      return conceptData;
  }
  
  /** 予算 (budgetInt) をフィルタリングカテゴリ (low, medium, high) に変換 */
  public String getRegionBudget(String budget) {
	  int budgetInt=Integer.parseInt(budget);
      if (budgetInt <= 35) return "low";    // 格安
      if (budgetInt <= 50) return "medium"; // 普通
      return "high";                             // 豪華
  }

  /** フライト時間 (flightTimeString) をフィルタリングカテゴリ (short, medium, long) に変換 */
  public String getFlightTimeCategory(String flightTime) {
	  int flightTimeInt=convertFlightTimeToInt(flightTime);
      if (flightTimeInt < 10) {
          return "short"; // 10時間未満
      } else if (flightTimeInt <= 15) {
          return "medium"; // 10時間～15時間
      } else {
          return "long"; // 15時間超
      }
  }
  
  public Integer convertFlightTimeToInt(String timeStr) {
	    String cleanedStr = timeStr.replaceAll("[^0-9\\-～]", "");
	    String[] parts;
	    if (cleanedStr.contains("～")) {
	        parts = cleanedStr.split("～");
	    } else if (cleanedStr.contains("-")) {
	        parts = cleanedStr.split("-");
	    } else {
	        try {
	            return Integer.parseInt(cleanedStr);
	        } catch (NumberFormatException e) {
	            return 999;
	        }
	    }

	    try {
	        if (parts.length == 2) {
	            double start = Double.parseDouble(parts[0]);
	            double end = Double.parseDouble(parts[1]);
	            double average = (start + end) / 2.0;
	            return (int) Math.round(average);
	        } else {
	            return 999; 
	        }
	    } catch (NumberFormatException e) {
	        return 999;
	    }
	}
  
    
    // ✅ 条件でフィルタリング（予算・気候など）
  public List<Region> getFilteredRegions(String budget, String time, String timezone, String concept) {
      return regionData.stream()
              .filter(region ->
                      (budget.isEmpty() || region.getBudget().equals(budget)) &&
                      (time.isEmpty() || region.getFlightTime().equals(time)) &&
                      (timezone.isEmpty() || matchTimezone(region.getTimezone(), timezone)) &&
                      (concept.isEmpty() || hasConcept(region, concept))
              )
              .collect(Collectors.toList());
  }
    
    private boolean hasConcept(Region region, String conceptName) {
        if (region.getConcepts() == null) return false;
        return region.getConcepts().stream()
            .anyMatch(c -> c.getName().equalsIgnoreCase(conceptName));
    }

    // ✅ 時差フィルタ（数値で比較）
    private boolean matchTimezone(String tzStr, String selectedRange) {
        if (selectedRange == null || selectedRange.isEmpty()) return true;
        if (tzStr == null || tzStr.isEmpty()) return false;

        try {
            // ✅ 数字部分だけ抽出（例: "UTC+9" → "9"、"GMT-3" → "-3"）
            String cleaned = tzStr.replaceAll("[^0-9\\-\\.]", "");
            if (cleaned.isEmpty()) return false;

            double tz = Double.parseDouble(cleaned);

            // ✅ 特殊値対応
            if (selectedRange.endsWith("_plus")) return tz >= 12;
            if (selectedRange.endsWith("_minus")) return tz <= -12;

            // ✅ 範囲値をパース
            boolean isPositive = selectedRange.startsWith("+");
            String[] parts = selectedRange.substring(1).split("_");
            if (parts.length != 2) return true;

            double start = Double.parseDouble(parts[0]);
            double end = Double.parseDouble(parts[1]);

            if (isPositive) {
                return tz >= start && tz < end;
            } else {
                // 例: selectedRange = "-3_6" → tz=-4 はマッチ
                return tz > -end && tz <= -start;
            }
        } catch (Exception e) {
            return false;
        }
    }

  

    // テンプレートエラー解消のためのヘルパーメソッド
    public static String getTimezoneStr(Region region) {
        if (region.getTimezone() == null || region.getTimezone().isEmpty()) return "不明";
        
        try {
            double tz = Double.parseDouble(region.getTimezone());
            if (tz == 0) return "±0";
            return (tz > 0 ? "+" : "") + tz;
        } catch (NumberFormatException e) {
            return region.getTimezone();
        }
    }
 }