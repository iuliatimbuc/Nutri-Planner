package com.example.nutri_planner.repository;

import com.example.nutri_planner.model.User;
import com.example.nutri_planner.model.WaterLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WaterLogRepository extends JpaRepository<WaterLog, Long> {

    // toate logurile de apa dintr-o zi
    List<WaterLog> findByUserAndLogDate(User user, LocalDate logDate);

    // totalul ml dintr-o zi
    @Query("SELECT SUM(w.amountMl) FROM WaterLog w " +
            "WHERE w.user = :user AND w.logDate = :logDate")
    Integer sumAmountByUserAndLogDate(@Param("user") User user,
                                      @Param("logDate") LocalDate logDate);

    // pentru istoric
    @Query("SELECT DISTINCT w.logDate FROM WaterLog w " +
            "WHERE w.user = :user ORDER BY w.logDate DESC")
    List<LocalDate> findDistinctLogDatesByUser(@Param("user") User user);
}