package com.example.nutriplanner.service;

import com.example.nutriplanner.model.WeightLog;

import java.time.LocalDate;
import java.util.List;

public interface WeightLogService {
    WeightLog addWeightLog(Long userId, double weightKg, LocalDate date);

    WeightLog getWeightLogById(Long id);

    List<WeightLog> getWeightHistory(Long userId);

    WeightLog updateWeightLog(Long logId, double newWeightKg);

    void deleteWeightLog(Long logId);
}
