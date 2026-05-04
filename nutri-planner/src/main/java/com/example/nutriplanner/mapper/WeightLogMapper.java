package com.example.nutriplanner.mapper;

import com.example.nutriplanner.dto.WeightLogDTO;
import com.example.nutriplanner.model.WeightLog;

public class WeightLogMapper {

    public static WeightLogDTO toDto(WeightLog log) {
        return WeightLogDTO.builder()
                .id(log.getId())
                .userId(log.getUser().getId())
                .weightNow(log.getWeightNow())
                .logDate(log.getLogDate())
                .bmi(log.getBmi())
                .differenceFromTarget(log.getDifferenceFromTarget())
                .differenceFromStart(log.getDifferenceFromStart())
                .build();
    }
}