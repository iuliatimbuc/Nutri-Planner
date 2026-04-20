package com.example.nutriplanner.model;


import com.example.nutriplanner.constants.ActivityLevel;
import com.example.nutriplanner.constants.Gender;
import com.example.nutriplanner.constants.Goal;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private int age;
    private Gender gender;
    private double weight;
    private double initialWeight;
    private double height;
    private double targetWeight;
    private LocalDate targetDate;
    private Goal goal;
    private ActivityLevel activityLevel;
    private int dailyCalorieGoal;
    private int dailyProteinGoal;
    private int dailyCarbsGoal;
    private int dailyFatGoal;
}
