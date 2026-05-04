package com.example.nutriplanner.mapper;

import com.example.nutriplanner.dto.DailyLogDTO;
import com.example.nutriplanner.model.DailyLog;

public class DailyLogMapper {

    public static DailyLogDTO toDto(DailyLog log) {
        return DailyLogDTO.builder()
                .id(log.getId())
                .userId(log.getUser().getId())
                .foodId(log.getFood().getId())
                .foodName(log.getFood().getName())
                .quantity(log.getQuantity())
                .mealType(log.getMealType())
                .logDate(log.getLogDate())
                .calculatedCalories(log.getCalculatedCalories())
                .calculatedProtein(log.getCalculatedProtein())
                .calculatedCarbs(log.getCalculatedCarbs())
                .calculatedFat(log.getCalculatedFat())
                .build();
    }
}