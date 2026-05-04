package com.example.nutriplanner.service.impl;

import com.example.nutriplanner.constants.MealType;
import com.example.nutriplanner.exceptions.ApiExceptionResponse;
import com.example.nutriplanner.model.DailyLog;
import com.example.nutriplanner.model.Food;
import com.example.nutriplanner.model.User;
import com.example.nutriplanner.repository.DailyLogRepository;
import com.example.nutriplanner.service.DailyLogService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class DailyLogServiceImp implements DailyLogService {

    private final DailyLogRepository dailyLogsRepository;
    private final UserServiceImp userService;
    private final FoodServiceImp foodService;

    public DailyLogServiceImp(DailyLogRepository dailyLogsRepository, UserServiceImp userService, FoodServiceImp foodService) {
        this.dailyLogsRepository = dailyLogsRepository;
        this.userService = userService;
        this.foodService = foodService;
    }

    public DailyLog addLog(Long userId, Long foodId, Double quantity, MealType mealType, LocalDate date) throws ApiExceptionResponse{
        User user = userService.getUserById(userId);
        Food food = foodService.getFoodById(foodId);
        double ratio = quantity / food.getServingSize();

        DailyLog log = new DailyLog();
        log.setUser(user);
        log.setFood(food);
        log.setQuantity(quantity);
        log.setMealType(mealType);
        log.setLogDate(date != null ? date : LocalDate.now());
        log.setCalculatedCalories(food.getCalories() * ratio);
        log.setCalculatedProtein(food.getProtein() * ratio);
        log.setCalculatedCarbs(food.getCarbs() * ratio);
        log.setCalculatedFat(food.getFat() * ratio);
        return dailyLogsRepository.save(log);
    }

    public DailyLog getLogById(Long id) throws ApiExceptionResponse{
        DailyLog log = dailyLogsRepository.findById(id).orElse(null);
        if (log == null) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("No log with id " + id))
                    .message("Entity not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        return log;
    }

    public List<DailyLog> getLogsByMealAndDate(Long userId, MealType mealType, LocalDate date) throws ApiExceptionResponse{
        User user = userService.getUserById(userId);
        return dailyLogsRepository.findByUserAndLogDateAndMealType(user, date, mealType);
    }

    public double getTotalCaloriesByDate(Long userId, LocalDate date)throws ApiExceptionResponse {
        User user = userService.getUserById(userId);
        return dailyLogsRepository.findByUserAndLogDate(user, date)
                .stream()
                .mapToDouble(DailyLog::getCalculatedCalories)
                .sum();
    }

    public double getTotalProteinByDate(Long userId, LocalDate date) throws ApiExceptionResponse{
        User user = userService.getUserById(userId);
        return dailyLogsRepository.findByUserAndLogDate(user, date)
                .stream()
                .mapToDouble(DailyLog::getCalculatedProtein)
                .sum();
    }

    public double getTotalCarbsByDate(Long userId, LocalDate date) throws ApiExceptionResponse{
        User user = userService.getUserById(userId);
        return dailyLogsRepository.findByUserAndLogDate(user, date)
                .stream()
                .mapToDouble(DailyLog::getCalculatedCarbs)
                .sum();
    }

    public double getTotalFatByDate(Long userId, LocalDate date) throws ApiExceptionResponse{
        User user = userService.getUserById(userId);
        return dailyLogsRepository.findByUserAndLogDate(user, date)
                .stream()
                .mapToDouble(DailyLog::getCalculatedFat)
                .sum();
    }

    public double getCaloriesByMealAndDate(Long userId, MealType mealType, LocalDate date) throws ApiExceptionResponse {
        User user = userService.getUserById(userId);
        return dailyLogsRepository.findByUserAndLogDateAndMealType(user, date, mealType)
                .stream()
                .mapToDouble(DailyLog::getCalculatedCalories)
                .sum();
    }

    public Map<LocalDate, Double> getMonthlyCaloriesSummary(Long userId, int year, int month) throws ApiExceptionResponse {
        User user = userService.getUserById(userId);
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        Map<LocalDate, Double> summary = new HashMap<>();

        for (LocalDate current = start; !current.isAfter(end); current = current.plusDays(1)) {
            double total = dailyLogsRepository.findByUserAndLogDate(user, current)
                    .stream()
                    .mapToDouble(DailyLog::getCalculatedCalories)
                    .sum();
            if (total > 0) {
                summary.put(current, total);
            }
        }
        return summary;
    }

    public DailyLog updateLog(Long logId, Double newQuantity, MealType newMealType) throws ApiExceptionResponse{
        DailyLog existing = getLogById(logId);
        Food food = existing.getFood();
        double ratio = newQuantity / food.getServingSize();

        existing.setQuantity(newQuantity);
        existing.setMealType(newMealType);
        existing.setCalculatedCalories(food.getCalories() * ratio);
        existing.setCalculatedProtein(food.getProtein() * ratio);
        existing.setCalculatedCarbs(food.getCarbs() * ratio);
        existing.setCalculatedFat(food.getFat() * ratio);
        return dailyLogsRepository.save(existing);
    }

    public void deleteLog(Long logId) throws ApiExceptionResponse{
        getLogById(logId);
        dailyLogsRepository.deleteById(logId);
    }
}