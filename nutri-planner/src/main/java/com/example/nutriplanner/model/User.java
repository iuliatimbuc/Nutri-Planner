package com.example.nutriplanner.model;

import com.example.nutriplanner.constants.ActivityLevel;
import com.example.nutriplanner.constants.Gender;
import com.example.nutriplanner.constants.Goal;
import com.example.nutriplanner.validation.MinTwoWeeksFromNow;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 20, message = "Size must be between {min} and {max}")
    private String name;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email must be in format: example@domain.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Min(value = 0, message = "Age must be positive")
    @Max(value = 120, message = "Age must be realistic")
    private int age;

    @Positive(message = "Weight must be positive")
    @DecimalMin(value = "20.0", message = "Weight cannot be less than 20kg")
    @DecimalMax(value = "500.0", message = "Weight cannot exceed 500kg")
    private double weight;

    @Positive(message = "Height must be positive")
    @DecimalMin(value = "50.0", message = "Height cannot be less than 50cm")
    @DecimalMax(value = "250.0", message = "Height cannot exceed 250cm")
    private double height;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull(message = "Massage is required")
    @Enumerated(EnumType.STRING)
    private Goal goal;

    @NotNull(message = "Activity lavel is required")
    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @Positive(message = "Weight must be positive")
    @DecimalMin(value = "20.0", message = "Weight cannot be less than 20kg")
    @DecimalMax(value = "500.0", message = "Weight cannot exceed 500kg")
    private double initialWeight;

    @Positive(message = "Weight must be positive")
    @DecimalMin(value = "20.0", message = "Weight cannot be less than 20kg")
    @DecimalMax(value = "500.0", message = "Weight cannot exceed 500kg")
    private double targetWeight;

    @MinTwoWeeksFromNow
    private LocalDate targetDate;

    private int dailyCalorieGoal;
    private int dailyProteinGoal;
    private int dailyCarbsGoal;
    private int dailyFatGoal;
}