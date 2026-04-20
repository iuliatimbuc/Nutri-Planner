package com.example.nutriplanner.model;

import com.example.nutriplanner.constants.MealType;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class DailyLog {
    private Long id;
    private User user;
    private Food food;
    private double quantity;
    private MealType mealType;
    private LocalDate logDate;
    private double calculatedCalories;
    private double calculatedProtein;
    private double calculatedCarbs;
    private double calculatedFat;
}
