package com.example.nutriplanner.model;


import com.example.nutriplanner.constants.Goal;
import com.example.nutriplanner.constants.Unit;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Food {
    private Long id;
    private String name;
    private double calories;
    private double protein;
    private double carbs;
    private double fat;
    private double servingSize;
    private Unit unit;
    private LocalDate logDate;
}
