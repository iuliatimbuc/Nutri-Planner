package com.example.nutriplanner.repository;


import com.example.nutriplanner.model.User;
import com.example.nutriplanner.model.WaterLog;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class WaterLogRepository {

    private final List<WaterLog> logs = new ArrayList<>();
    private Long nextId = 1L;

    public WaterLog save(WaterLog log) {
        if (log.getId() == null) {
            log.setId(nextId++);
            logs.add(log);
        } else {
            logs.removeIf(l -> l.getId().equals(log.getId()));
            logs.add(log);
        }
        return log;
    }

    public WaterLog findById(Long id) {
        return logs.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<WaterLog> findAll() {
        return new ArrayList<>(logs);
    }

    public Integer sumAmountByUserAndLogDate(User user, LocalDate date) {
        return logs.stream()
                .filter(l -> l.getUser().getId().equals(user.getId())
                        && l.getLogDate().equals(date))
                .mapToInt(WaterLog::getAmountMl)
                .sum();
    }

    public List<LocalDate> findDistinctLogDatesByUser(User user) {
        return logs.stream()
                .filter(l -> l.getUser().getId().equals(user.getId()))
                .map(WaterLog::getLogDate)
                .distinct()
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        logs.removeIf(l -> l.getId().equals(id));
    }
}