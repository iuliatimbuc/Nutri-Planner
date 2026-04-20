package com.example.nutri_planner.service;

import com.example.nutri_planner.model.DailyLog;
import com.example.nutri_planner.model.Food;
import com.example.nutri_planner.model.MealType;
import com.example.nutri_planner.model.User;
import com.example.nutri_planner.repository.DailyLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class DailyLogService {

    @Autowired
    private DailyLogRepository dailyLogsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodService foodService;

    public DailyLog addLog(Long userId, Long foodId, Double quantity, MealType mealType) {
        User user = userService.getUserById(userId);
        Food food = foodService.getFoodById(foodId);

        // calculează valorile nutritionale in functie de cantitate
        double ratio = quantity / food.getServingSize();

        DailyLog log = new DailyLog();
        log.setUser(user);
        log.setFood(food);
        log.setQuantity(quantity);
        log.setMealType(mealType);
        log.setCalculatedCalories(food.getCalories() * ratio);
        log.setCalculatedProtein(food.getProtein() * ratio);
        log.setCalculatedCarbs(food.getCarbs() * ratio);
        log.setCalculatedFat(food.getFat() * ratio);

        return dailyLogsRepository.save(log);
    }

    public DailyLog getLogById(Long id) {
        return dailyLogsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DailyLog Not Found"));
    }

    // toate alimentele adaugate azi
    public List<DailyLog> getTodayLogs(Long userId) {
        User user = userService.getUserById(userId);
        return dailyLogsRepository.findByUserAndLogDate(user, LocalDate.now());
    }

    // toate alimentele adaugate de utilizator in trecut
    public List<DailyLog> getLogsByDate(Long userId, LocalDate date) {
        User user = userService.getUserById(userId);
        return dailyLogsRepository.findByUserAndLogDate(user, date);
    }

    // alimente dintr o masa specifica de azi
    public List<DailyLog> getTodayLogsByMeal(Long userId, MealType mealType) {
        User user = userService.getUserById(userId);
        return dailyLogsRepository.findByUserAndLogDateAndMealType(user, LocalDate.now(), mealType);
    }

    // alimente dintr o masa specifica intr o zi anume
    public List<DailyLog> getLogsByMeal(Long userId, MealType mealType, LocalDate date) {
        User user = userService.getUserById(userId);
        return dailyLogsRepository.findByUserAndLogDateAndMealType(user, date, mealType);
    }

    // toate zilele din istoric in care s a adaugat mancare
    public List<LocalDate> getLogHistory(Long userId) {
        User user = userService.getUserById(userId);
        return dailyLogsRepository.findDistinctLogDatesByUser(user);
    }

    public double getTodayTotalCalories(Long userId) {
        User user = userService.getUserById(userId);
        Double total = dailyLogsRepository.sumCaloriesByUserAndLogDate(user, LocalDate.now());
        return total != null ? total : 0.0;
    }

    public double getTodayTotalCarbs(Long userId) {
        User user = userService.getUserById(userId);
        Double total = dailyLogsRepository.sumCarbsByUserAndLogDate(user, LocalDate.now());
        return total != null ? total : 0.0;
    }

    public double getTodayTotalProtein(Long userId) {
        User user = userService.getUserById(userId);
        Double total = dailyLogsRepository.sumProteinByUserAndLogDate(user, LocalDate.now());
        return total != null ? total : 0.0;
    }

    public double getTodayTotalFat(Long userId) {
        User user = userService.getUserById(userId);
        Double total = dailyLogsRepository.sumFatByUserAndLogDate(user, LocalDate.now());
        return total != null ? total : 0.0;
    }

    public double getCarbsByMeal(Long userId, MealType mealType) {
        User user = userService.getUserById(userId);
        Double total = dailyLogsRepository.sumCarbsByUserAndLogDateAndMealType(
                user, LocalDate.now(), mealType);
        return total != null ? total : 0.0;
    }

    public double getCaloriesByMeal(Long userId, MealType mealType) {
        User user = userService.getUserById(userId);
        Double total = dailyLogsRepository.sumCaloriesByUserAndLogDateAndMealType(
                user, LocalDate.now(), mealType);
        return total != null ? total : 0.0;
    }

    public double getProteinByMeal(Long userId, MealType mealType) {
        User user = userService.getUserById(userId);
        Double total = dailyLogsRepository.sumProteinByUserAndLogDateAndMealType(
                user, LocalDate.now(), mealType);
        return total != null ? total : 0.0;
    }

    public double getFatByMeal(Long userId, MealType mealType) {
        User user = userService.getUserById(userId);
        Double total = dailyLogsRepository.sumFatByUserAndLogDateAndMealType(
                user, LocalDate.now(), mealType);
        return total != null ? total : 0.0;
    }

    public DailyLog updateLog(Long logId, Double newQuantity, MealType newMealType) {
        DailyLog existing = getLogById(logId);
        Food food = existing.getFood();

        // recalculeaza valorile nutritionale cu noua cantitate
        double ratio = newQuantity / food.getServingSize();

        existing.setQuantity(newQuantity);
        existing.setMealType(newMealType);
        existing.setCalculatedCalories(food.getCalories() * ratio);
        existing.setCalculatedProtein(food.getProtein() * ratio);
        existing.setCalculatedCarbs(food.getCarbs() * ratio);
        existing.setCalculatedFat(food.getFat() * ratio);

        return dailyLogsRepository.save(existing);
    }

    public void deleteLog(Long logId) {
        DailyLog log = getLogById(logId);

        if (!log.getLogDate().equals(LocalDate.now())) {
            throw new RuntimeException("Log Not Found");
        }

        dailyLogsRepository.deleteById(logId);
    }
}