package com.example.nutriplanner.repository;


import com.example.nutriplanner.model.User;
import com.example.nutriplanner.model.WeightLog;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class WeightLogRepository {

    private final List<WeightLog> logs = new ArrayList<>();
    private Long nextId = 1L;

    public WeightLog save(WeightLog log) {
        if (log.getId() == null) {
            log.setId(nextId++);
            logs.add(log);
        } else {
            logs.removeIf(l -> l.getId().equals(log.getId()));
            logs.add(log);
        }
        return log;
    }

    public WeightLog findById(Long id) {
        return logs.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public WeightLog findByUserAndLogDate(User user, LocalDate date) {
        return logs.stream()
                .filter(l -> l.getUser().getId().equals(user.getId())
                        && l.getLogDate().equals(date))
                .findFirst()
                .orElse(null);
    }

    public List<WeightLog> findByUserOrderByLogDateAsc(User user) {
        return logs.stream()
                .filter(l -> l.getUser().getId().equals(user.getId()))
                .sorted(Comparator.comparing(WeightLog::getLogDate))
                .collect(Collectors.toList());
    }

    public WeightLog findFirstByUserOrderByLogDateAsc(User user) {
        return logs.stream()
                .filter(l -> l.getUser().getId().equals(user.getId()))
                .min(Comparator.comparing(WeightLog::getLogDate))
                .orElse(null);
    }

}