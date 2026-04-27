package com.example.nutriplanner.service;


import com.example.nutriplanner.constants.MealType;
import com.example.nutriplanner.model.DailyLog;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DailyLogService {
    DailyLog addLog(Long userId, Long foodId, Double quantity, MealType mealType, LocalDate date);
    DailyLog getLogById(Long id);
    List<DailyLog> getLogsByMealAndDate(Long userId, MealType mealType, LocalDate date);
    double getTotalCaloriesByDate(Long userId, LocalDate date);
    double getTotalProteinByDate(Long userId, LocalDate date);
    double getTotalCarbsByDate(Long userId, LocalDate date);
    double getTotalFatByDate(Long userId, LocalDate date);
    double getCaloriesByMealAndDate(Long userId, MealType mealType, LocalDate date);
    DailyLog updateLog(Long logId, Double newQuantity, MealType newMealType);
    void deleteLog(Long logId);
    Map<LocalDate, Double> getMonthlyCaloriesSummary(Long userId, int year, int month);
}
