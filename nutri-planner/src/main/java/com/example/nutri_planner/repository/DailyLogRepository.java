package com.example.nutri_planner.repository;

import com.example.nutri_planner.model.DailyLog;
import com.example.nutri_planner.model.MealType;
import com.example.nutri_planner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {

    List<DailyLog> findByUserAndLogDate(User user, LocalDate logDate);

    // filtrare pe meal type intr o zi
    List<DailyLog> findByUserAndLogDateAndMealType(User user,
                                                    LocalDate logDate,
                                                    MealType mealType);

    // toate zilele care s-a adaugat macare
    @Query("SELECT DISTINCT d.logDate FROM DailyLog d " +
            "WHERE d.user = :user ORDER BY d.logDate DESC")
    List<LocalDate> findDistinctLogDatesByUser(@Param("user") User user);

    // totalul caloriilor dintr o zi
    @Query("SELECT SUM(d.calculatedCalories) FROM DailyLog d " +
            "WHERE d.user = :user AND d.logDate = :logDate")
    Double sumCaloriesByUserAndLogDate(@Param("user") User user,
                                       @Param("logDate") LocalDate logDate);

    // totalul proteinelor dintr-o zi
    @Query("SELECT SUM(d.calculatedProtein) FROM DailyLog d " +
            "WHERE d.user = :user AND d.logDate = :logDate")
    Double sumProteinByUserAndLogDate(@Param("user") User user,
                                      @Param("logDate") LocalDate logDate);

    // totalul carbohidraților dintr-o zi
    @Query("SELECT SUM(d.calculatedCarbs) FROM DailyLog d " +
            "WHERE d.user = :user AND d.logDate = :logDate")
    Double sumCarbsByUserAndLogDate(@Param("user") User user,
                                    @Param("logDate") LocalDate logDate);

    // totalul grasimilor dintr o zi
    @Query("SELECT SUM(d.calculatedFat) FROM DailyLog d " +
            "WHERE d.user = :user AND d.logDate = :logDate")
    Double sumFatByUserAndLogDate(@Param("user") User user,
                                  @Param("logDate") LocalDate logDate);

    // totalul caloriilor/proteinelor/carbs/grasimilor pentru o masa

    @Query("SELECT SUM(d.calculatedCalories) FROM DailyLog d " +
            "WHERE d.user = :user AND d.logDate = :logDate AND d.mealType = :mealType")
    Double sumCaloriesByUserAndLogDateAndMealType(@Param("user") User user,
                                                  @Param("logDate") LocalDate logDate,
                                                  @Param("mealType") MealType mealType);

    @Query("SELECT SUM(d.calculatedProtein) FROM DailyLog d " +
            "WHERE d.user = :user AND d.logDate = :logDate AND d.mealType = :mealType")
    Double sumProteinByUserAndLogDateAndMealType(@Param("user") User user,
                                                 @Param("logDate") LocalDate logDate,
                                                 @Param("mealType") MealType mealType);

    @Query("SELECT SUM(d.calculatedCarbs) FROM DailyLog d " +
            "WHERE d.user = :user AND d.logDate = :logDate AND d.mealType = :mealType")
    Double sumCarbsByUserAndLogDateAndMealType(@Param("user") User user,
                                               @Param("logDate") LocalDate logDate,
                                               @Param("mealType") MealType mealType);

    @Query("SELECT SUM(d.calculatedFat) FROM DailyLog d " +
            "WHERE d.user = :user AND d.logDate = :logDate AND d.mealType = :mealType")
    Double sumFatByUserAndLogDateAndMealType(@Param("user") User user,
                                             @Param("logDate") LocalDate logDate,
                                             @Param("mealType") MealType mealType);
}