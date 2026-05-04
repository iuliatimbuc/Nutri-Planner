package com.example.nutriplanner.mapper;

import com.example.nutriplanner.dto.WaterLogDTO;
import com.example.nutriplanner.model.WaterLog;

public class WaterLogMapper {

    public static WaterLogDTO toDto(WaterLog log) {
        return WaterLogDTO.builder()
                .id(log.getId())
                .userId(log.getUser().getId())
                .amountMl(log.getAmountMl())
                .logDate(log.getLogDate())
                .build();
    }
}