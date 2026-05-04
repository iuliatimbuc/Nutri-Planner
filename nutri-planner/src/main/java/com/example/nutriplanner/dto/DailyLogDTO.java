package com.example.nutriplanner.dto;

import com.example.nutriplanner.constants.MealType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DailyLogDTO {

    private Long id;
    @NotNull private Long userId;
    @NotNull private Long foodId;
    private String foodName;
    @Positive private double quantity;
    @NotNull private MealType mealType;
    @PastOrPresent private LocalDate logDate;

    private double calculatedCalories;
    private double calculatedProtein;
    private double calculatedCarbs;
    private double calculatedFat;
}