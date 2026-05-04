package com.example.nutriplanner.dto;

import com.example.nutriplanner.constants.Unit;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodCreationDTO {

    @NotBlank(message = "Food name is required")
    @Size(min = 2, max = 100)
    private String name;

    @PositiveOrZero private double calories;
    @PositiveOrZero private double protein;
    @PositiveOrZero private double carbs;
    @PositiveOrZero private double fat;

    @Positive private double servingSize;

    @NotNull private Unit unit;
}
