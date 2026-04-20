package com.example.nutriplanner.dto;


import com.example.nutriplanner.constants.MealType;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLogRequestDTO {
    private Long logId;
    private Double quantity;
    private MealType mealType;
}

