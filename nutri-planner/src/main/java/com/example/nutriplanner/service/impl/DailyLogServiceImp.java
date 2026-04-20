package com.example.nutriplanner.service.impl;


import com.example.nutriplanner.constants.MealType;
import com.example.nutriplanner.model.DailyLog;
import com.example.nutriplanner.model.Food;
import com.example.nutriplanner.model.User;
import com.example.nutriplanner.repository.DailyLogRepository;
import com.example.nutriplanner.service.DailyLogService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

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

    public DailyLog addLog(Long userId, Long foodId, Double quantity, MealType mealType, LocalDate date) {
        User user = this.userService.getUserById(userId);
        Food food = this.foodService.getFoodById(foodId);
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
        return this.dailyLogsRepository.save(log);
    }

    public DailyLog getLogById(Long id) {
        DailyLog log = this.dailyLogsRepository.findById(id);
        if (log == null) {
            throw new RuntimeException("DailyLog Not Found");
        } else {
            return log;
        }
    }

    public List<DailyLog> getTodayLogsByMeal(Long userId, MealType mealType) {
        User user = this.userService.getUserById(userId);
        return this.dailyLogsRepository.findByUserAndLogDateAndMealType(user, LocalDate.now(), mealType);
    }

    public Map<LocalDate, Double> getMonthlyCaloriesSummary(Long userId, int year, int month) {
        User user = this.userService.getUserById(userId);
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        Map<LocalDate, Double> summary = new HashMap();

        for(LocalDate current = start; !current.isAfter(end); current = current.plusDays(1L)) {
            Double total = this.dailyLogsRepository.sumCaloriesByUserAndLogDate(user, current);
            if (total != null && total > (double)0.0F) {
                summary.put(current, total);
            }
        }

        return summary;
    }

    public double getTodayTotalCalories(Long userId) {
        User user = this.userService.getUserById(userId);
        Double total = this.dailyLogsRepository.sumCaloriesByUserAndLogDate(user, LocalDate.now());
        return total != null ? total : (double)0.0F;
    }

    public double getTodayTotalCarbs(Long userId) {
        User user = this.userService.getUserById(userId);
        Double total = this.dailyLogsRepository.sumCarbsByUserAndLogDate(user, LocalDate.now());
        return total != null ? total : (double)0.0F;
    }

    public double getTodayTotalProtein(Long userId) {
        User user = this.userService.getUserById(userId);
        Double total = this.dailyLogsRepository.sumProteinByUserAndLogDate(user, LocalDate.now());
        return total != null ? total : (double)0.0F;
    }

    public double getTodayTotalFat(Long userId) {
        User user = this.userService.getUserById(userId);
        Double total = this.dailyLogsRepository.sumFatByUserAndLogDate(user, LocalDate.now());
        return total != null ? total : (double)0.0F;
    }

    public double getCaloriesByMeal(Long userId, MealType mealType) {
        User user = this.userService.getUserById(userId);
        Double total = this.dailyLogsRepository.sumCaloriesByUserAndLogDateAndMealType(user, LocalDate.now(), mealType);
        return total != null ? total : (double)0.0F;
    }

    public List<DailyLog> getLogsByMealAndDate(Long userId, MealType mealType, LocalDate date) {
        User user = this.userService.getUserById(userId);
        return this.dailyLogsRepository.findByUserAndLogDateAndMealType(user, date, mealType);
    }

    public double getTotalCaloriesByDate(Long userId, LocalDate date) {
        User user = this.userService.getUserById(userId);
        Double total = this.dailyLogsRepository.sumCaloriesByUserAndLogDate(user, date);
        return total != null ? total : (double)0.0F;
    }

    public double getTotalProteinByDate(Long userId, LocalDate date) {
        User user = this.userService.getUserById(userId);
        Double total = this.dailyLogsRepository.sumProteinByUserAndLogDate(user, date);
        return total != null ? total : (double)0.0F;
    }

    public double getTotalCarbsByDate(Long userId, LocalDate date) {
        User user = this.userService.getUserById(userId);
        Double total = this.dailyLogsRepository.sumCarbsByUserAndLogDate(user, date);
        return total != null ? total : (double)0.0F;
    }

    public double getTotalFatByDate(Long userId, LocalDate date) {
        User user = this.userService.getUserById(userId);
        Double total = this.dailyLogsRepository.sumFatByUserAndLogDate(user, date);
        return total != null ? total : (double)0.0F;
    }

    public double getCaloriesByMealAndDate(Long userId, MealType mealType, LocalDate date) {
        User user = this.userService.getUserById(userId);
        Double total = this.dailyLogsRepository.sumCaloriesByUserAndLogDateAndMealType(user, date, mealType);
        return total != null ? total : (double)0.0F;
    }

    public DailyLog updateLog(Long logId, Double newQuantity, MealType newMealType) {
        DailyLog existing = this.getLogById(logId);
        Food food = existing.getFood();
        double ratio = newQuantity / food.getServingSize();
        existing.setQuantity(newQuantity);
        existing.setMealType(newMealType);
        existing.setCalculatedCalories(food.getCalories() * ratio);
        existing.setCalculatedProtein(food.getProtein() * ratio);
        existing.setCalculatedCarbs(food.getCarbs() * ratio);
        existing.setCalculatedFat(food.getFat() * ratio);
        return this.dailyLogsRepository.save(existing);
    }

    public void deleteLog(Long logId) {
        this.getLogById(logId);
        this.dailyLogsRepository.deleteById(logId);
    }
}
