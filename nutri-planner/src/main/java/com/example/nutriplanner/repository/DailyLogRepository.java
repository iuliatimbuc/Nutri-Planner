package com.example.nutriplanner.repository;

import com.example.nutriplanner.constants.MealType;
import com.example.nutriplanner.model.DailyLog;
import com.example.nutriplanner.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyLogRepository extends CrudRepository<DailyLog, Long> {
    List<DailyLog> findByUserAndLogDateAndMealType(User user, LocalDate date, MealType mealType);
    List<DailyLog> findByUserAndLogDate(User user, LocalDate date);
}