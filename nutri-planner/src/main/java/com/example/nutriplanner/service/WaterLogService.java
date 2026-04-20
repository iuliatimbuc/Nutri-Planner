package com.example.nutriplanner.service;

import com.example.nutriplanner.model.WaterLog;

import java.time.LocalDate;
import java.util.List;

public interface WaterLogService {
    WaterLog addWaterLog(Long userId, int amountMl, LocalDate date);

    WaterLog getWaterLogById(Long id);

    int getTodayTotal(Long userId);

    int getTotalByDate(Long userId, LocalDate date);

    List<LocalDate> getLogHistory(Long userId);

    WaterLog updateWaterLog(Long logId, int newAmountMl);

    void deleteWaterLog(Long logId);
}
