package com.example.nutriplanner.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor  // Constructor fără argumente
@AllArgsConstructor
public class UserGoalsResponseDTO {
    private int dailyCalorieGoal;;
    private int dailyProteinGoal;
    private int dailyCarbsGoal;
    private int dailyFatGoal;

}

