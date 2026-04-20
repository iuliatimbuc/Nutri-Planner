package com.example.nutriplanner.repository;

import com.example.nutriplanner.constants.MealType;
import com.example.nutriplanner.model.DailyLog;
import com.example.nutriplanner.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DailyLogRepository {

    private final List<DailyLog> logs = new ArrayList<>();
    private Long nextId = 1L;

    public DailyLog save(DailyLog log) {
        if (log.getId() == null) {
            log.setId(nextId++);
            logs.add(log);
        } else {
            logs.removeIf(l -> l.getId().equals(log.getId()));
            logs.add(log);
        }
        return log;
    }

    public DailyLog findById(Long id) {
        return logs.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<DailyLog> findAll() {
        return new ArrayList<>(logs);
    }

    public List<DailyLog> findByUserAndLogDateAndMealType(User user, LocalDate date, MealType mealType) {
        return logs.stream()
                .filter(l -> l.getUser().getId().equals(user.getId())
                        && l.getLogDate().equals(date)
                        && l.getMealType().equals(mealType))
                .collect(Collectors.toList());
    }

    public Double sumCaloriesByUserAndLogDate(User user, LocalDate date) {
        return logs.stream()
                .filter(l -> l.getUser().getId().equals(user.getId())
                        && l.getLogDate().equals(date))
                .mapToDouble(DailyLog::getCalculatedCalories)
                .sum();
    }

    public Double sumProteinByUserAndLogDate(User user, LocalDate date) {
        return logs.stream()
                .filter(l -> l.getUser().getId().equals(user.getId())
                        && l.getLogDate().equals(date))
                .mapToDouble(DailyLog::getCalculatedProtein)
                .sum();
    }

    public Double sumCarbsByUserAndLogDate(User user, LocalDate date) {
        return logs.stream()
                .filter(l -> l.getUser().getId().equals(user.getId())
                        && l.getLogDate().equals(date))
                .mapToDouble(DailyLog::getCalculatedCarbs)
                .sum();
    }

    public Double sumFatByUserAndLogDate(User user, LocalDate date) {
        return logs.stream()
                .filter(l -> l.getUser().getId().equals(user.getId())
                        && l.getLogDate().equals(date))
                .mapToDouble(DailyLog::getCalculatedFat)
                .sum();
    }

    public Double sumCaloriesByUserAndLogDateAndMealType(User user, LocalDate date, MealType mealType) {
        return logs.stream()
                .filter(l -> l.getUser().getId().equals(user.getId())
                        && l.getLogDate().equals(date)
                        && l.getMealType().equals(mealType))
                .mapToDouble(DailyLog::getCalculatedCalories)
                .sum();
    }

    public void deleteById(Long id) {
        logs.removeIf(l -> l.getId().equals(id));
    }
}