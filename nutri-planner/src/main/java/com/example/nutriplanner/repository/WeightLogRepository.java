package com.example.nutriplanner.repository;

import com.example.nutriplanner.model.User;
import com.example.nutriplanner.model.WeightLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeightLogRepository extends CrudRepository<WeightLog, Long> {
    Optional<WeightLog> findByUserAndLogDate(User user, LocalDate date);
    List<WeightLog> findByUserOrderByLogDateAsc(User user);
}