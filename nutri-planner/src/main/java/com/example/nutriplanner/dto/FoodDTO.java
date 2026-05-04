package com.example.nutriplanner.dto;

import com.example.nutriplanner.constants.Unit;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class FoodDTO {

    private Long id;

    @NotBlank(message = "Food name is required")
    @Size(min = 2, max = 100, message = "Name must be between {min} and {max} characters")
    private String name;

    @PositiveOrZero(message = "Calories must be positive")
    @DecimalMax(value = "10000.0", message = "Calories cannot exceed 10000")
    private double calories;

    @PositiveOrZero(message = "Protein must be positive")
    @DecimalMax(value = "1000.0", message = "Protein cannot exceed 1000g")
    private double protein;

    @PositiveOrZero(message = "Carbs must be positive")
    @DecimalMax(value = "1000.0", message = "Carbs cannot exceed 1000g")
    private double carbs;

    @PositiveOrZero(message = "Fat must be positive")
    @DecimalMax(value = "1000.0", message = "Fat cannot exceed 1000g")
    private double fat;

    @Positive(message = "Serving size must be positive")
    private double servingSize;

    @NotNull(message = "Unit is required")
    private Unit unit;
}