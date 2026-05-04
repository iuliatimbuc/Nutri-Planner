package com.example.nutriplanner.dto;

import com.example.nutriplanner.constants.ActivityLevel;
import com.example.nutriplanner.constants.Gender;
import com.example.nutriplanner.constants.Goal;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UserDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between {min} and {max} characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email must be valid (ex: user@domain.com)")
    private String email;

    @Min(value = 1, message = "Age must be positive")
    @Max(value = 120, message = "Age must be realistic")
    private int age;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Positive(message = "Weight must be positive")
    @DecimalMax(value = "500.0", message = "Weight cannot exceed 500kg")
    private double weight;

    @Positive(message = "Height must be positive")
    @DecimalMax(value = "250.0", message = "Height cannot exceed 250cm")
    private double height;

    @Positive(message = "Target weight must be positive")
    private double targetWeight;

    @Future(message = "Target date must be in the future")
    private LocalDate targetDate;

    @NotNull(message = "Goal is required")
    private Goal goal;

    @NotNull(message = "Activity level is required")
    private ActivityLevel activityLevel;

    private double initialWeight;

    private int dailyCalorieGoal;
    private int dailyProteinGoal;
    private int dailyCarbsGoal;
    private int dailyFatGoal;
}