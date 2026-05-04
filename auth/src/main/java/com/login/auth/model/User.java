package com.login.auth.model;

import com.login.auth.constants.ActivityLevel;
import com.login.auth.constants.Gender;
import com.login.auth.constants.Goal;
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
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email must be in format: example@domain.com"
    )
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Min(value = 0, message = "Age must be positive")
    @Max(value = 120, message = "Age must be realistic")
    private int age;

    @Positive(message = "Weight must be positive")
    private double weight;

    @Positive(message = "Height must be positive")
    private double height;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Goal goal;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    private double initialWeight;
    private double targetWeight;
    private LocalDate targetDate;
    private int dailyCalorieGoal;
    private int dailyProteinGoal;
    private int dailyCarbsGoal;
    private int dailyFatGoal;
}