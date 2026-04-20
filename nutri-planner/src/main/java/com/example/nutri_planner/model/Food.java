package com.example.nutri_planner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "food")
@Getter
@Setter
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int calories;
    private int protein;
    private int carbs;
    private int fat;

    @Enumerated(EnumType.STRING)
    private Unit unit;
    private int servingSize;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyLog> dailyLogs = new ArrayList<>();

}
