package com.example.nutriplanner.dto;

import com.example.nutriplanner.constants.ActivityLevel;
import com.example.nutriplanner.constants.Gender;
import com.example.nutriplanner.constants.Goal;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class UserCreationDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Min(value = 1) @Max(value = 120)
    private int age;

    @NotNull private Gender gender;

    @Positive private double weight;
    @Positive private double height;
    @Positive private double targetWeight;

    @Future private LocalDate targetDate;

    @NotNull private Goal goal;
    @NotNull private ActivityLevel activityLevel;
}