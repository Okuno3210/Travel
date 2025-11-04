package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "concept")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"regions"})
public class Concept {
	
	@ManyToMany(mappedBy = "concepts")
	private List<Region> regions;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;      // CSVのid（自動採番でもCSV値でもOK）

    private String name;  // 「美術」「グルメ」「絶景」など
}
