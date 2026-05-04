package com.example.nutriplanner.model;

import com.example.nutriplanner.constants.Unit;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "foods")
@Getter
@Setter
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Food name is required")
    @Size(min = 2, max = 100, message = "Name must be between {min} and {max} characters")
    @Pattern(regexp = "^[A-Za-z0-9 ._-]+$", message = "Name must contain only letters and numbers")
    private String name;

    @PositiveOrZero(message = "Calories must be positive")
    @Max(value = 10000, message = "Calories cannot exceed 10000")
    private double calories;

    @PositiveOrZero(message = "Protein must be positive")
    @Max(value = 1000, message = "Protein cannot exceed 1000g")
    private double protein;

    @PositiveOrZero(message = "Carbs must be positive")
    @Max(value = 1000, message = "Carbs cannot exceed 1000g")
    private double carbs;

    @PositiveOrZero(message = "Fat must be positive")
    @Max(value = 1000, message = "Fat cannot exceed 1000g")
    private double fat;

    @Positive(message = "Serving size must be positive")
    @Max(value = 10000, message = "Serving size cannot exceed 10000")
    private double servingSize;

    @NotNull(message = "Unit is required")
    @Enumerated(EnumType.STRING)
    private Unit unit;

}