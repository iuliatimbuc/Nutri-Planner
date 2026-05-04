package com.example.nutriplanner.service;

import com.example.nutriplanner.exceptions.ApiExceptionResponse;
import com.example.nutriplanner.model.WeightLog;

import java.time.LocalDate;
import java.util.List;

public interface WeightLogService {
    WeightLog addWeightLog(Long userId, double weightKg, LocalDate date) throws ApiExceptionResponse;
    WeightLog getWeightLogById(Long id) throws ApiExceptionResponse;
    List<WeightLog> getWeightHistory(Long userId) throws ApiExceptionResponse;
    WeightLog updateWeightLog(Long logId, double newWeightKg) throws ApiExceptionResponse;

}
