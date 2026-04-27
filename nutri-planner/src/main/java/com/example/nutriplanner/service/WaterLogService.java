package com.example.nutriplanner.service;

import com.example.nutriplanner.model.WaterLog;

import java.time.LocalDate;
import java.util.List;

public interface WaterLogService {
    WaterLog addWaterLog(Long userId, int amountMl, LocalDate date);
    int getTotalByDate(Long userId, LocalDate date);

}
