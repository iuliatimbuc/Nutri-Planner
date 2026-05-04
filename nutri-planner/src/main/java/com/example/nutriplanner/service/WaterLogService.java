package com.example.nutriplanner.service;

import com.example.nutriplanner.exceptions.ApiExceptionResponse;
import com.example.nutriplanner.model.WaterLog;

import java.time.LocalDate;
import java.util.List;

public interface WaterLogService {
    WaterLog addWaterLog(Long userId, int amountMl, LocalDate date) throws ApiExceptionResponse;
    int getTotalByDate(Long userId, LocalDate date) throws ApiExceptionResponse;
    public WaterLog updateWaterLog(Long userId, int amountMl, LocalDate date) throws ApiExceptionResponse;

}
