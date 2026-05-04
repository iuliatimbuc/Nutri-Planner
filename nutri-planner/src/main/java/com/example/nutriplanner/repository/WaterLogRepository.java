package com.example.nutriplanner.repository;

import com.example.nutriplanner.model.User;
import com.example.nutriplanner.model.WaterLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface WaterLogRepository extends CrudRepository<WaterLog, Long> {
    List<WaterLog> findByUserAndLogDate(User user, LocalDate date);
}