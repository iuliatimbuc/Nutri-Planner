package com.example.nutri_planner.repository;

import com.example.nutri_planner.model.User;
import com.example.nutri_planner.model.WeightLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeightLogRepository extends JpaRepository<WeightLog, Long> {

    // istoricul complet sortat cronologic -> pt graficul de progres
    List<WeightLog> findByUserOrderByLogDateAsc(User user);

    // inregistrarea dintr-o zi specifica
    Optional<WeightLog> findByUserAndLogDate(User user, LocalDate logDate);

    // prima inregistrare
    Optional<WeightLog> findFirstByUserOrderByLogDateAsc(User user);

    // ultima inregistrare
    Optional<WeightLog> findFirstByUserOrderByLogDateDesc(User user);
}