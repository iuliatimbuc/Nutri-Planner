package com.example.nutriplanner.model;

import com.example.nutriplanner.constants.MealType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "daily_logs")
@Getter
@Setter
public class DailyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User is required")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "Food is required")
    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @Positive(message = "Quantity must be positive")
    @DecimalMax(value = "10000.0", message = "Quantity cannot exceed 10000g")
    private double quantity;

    @NotNull(message = "Meal type is required")
    @Enumerated(EnumType.STRING)
    private MealType mealType;

    private LocalDate logDate;
    private double calculatedCalories;
    private double calculatedProtein;
    private double calculatedCarbs;
    private double calculatedFat;
}