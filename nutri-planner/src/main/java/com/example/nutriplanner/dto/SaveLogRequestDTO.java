package com.example.nutriplanner.dto;


import com.example.nutriplanner.constants.MealType;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveLogRequestDTO {
    private Long userId;
    private Long foodId;
    private Double quantity;
    private MealType mealType;
    private LocalDate date;

}
