package com.example.nutriplanner.service.impl;

import com.example.nutriplanner.exceptions.ApiExceptionResponse;
import com.example.nutriplanner.model.User;
import com.example.nutriplanner.model.WeightLog;
import com.example.nutriplanner.repository.WeightLogRepository;
import com.example.nutriplanner.service.WeightLogService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WeightLogServiceImp implements WeightLogService {

    private final WeightLogRepository weightLogRepository;
    private final UserServiceImp userService;

    public WeightLogServiceImp(WeightLogRepository weightLogRepository, UserServiceImp userService) {
        this.weightLogRepository = weightLogRepository;
        this.userService = userService;
    }

    public WeightLog addWeightLog(Long userId, double weightKg, LocalDate date) throws ApiExceptionResponse {
        User user = userService.getUserById(userId);

        WeightLog log = weightLogRepository.findByUserAndLogDate(user, date)
                .orElse(new WeightLog());

        log.setUser(user);
        log.setLogDate(date);
        log.setWeightNow(weightKg);

        double heightM = user.getHeight() / 100.0;
        log.setBmi(Math.round((weightKg / (heightM * heightM)) * 100.0) / 100.0);
        log.setDifferenceFromStart(Math.round((weightKg - user.getInitialWeight()) * 100.0) / 100.0);
        log.setDifferenceFromTarget(Math.round((weightKg - user.getTargetWeight()) * 100.0) / 100.0);

        if (date.equals(LocalDate.now())) {
            user.setWeight(weightKg);
            userService.updateUser(user.getId(), user);
        }

        return weightLogRepository.save(log);
    }

    public WeightLog getWeightLogById(Long id) throws ApiExceptionResponse {
        WeightLog log = weightLogRepository.findById(id).orElse(null);
        if (log == null) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("No weight log with id " + id))
                    .message("Entity not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        return log;
    }

    public List<WeightLog> getWeightHistory(Long userId) throws ApiExceptionResponse{
        User user = userService.getUserById(userId);
        return weightLogRepository.findByUserOrderByLogDateAsc(user);
    }

    public WeightLog updateWeightLog(Long logId, double newWeightKg) throws ApiExceptionResponse{
        WeightLog existing = getWeightLogById(logId);
        User user = existing.getUser();

        existing.setWeightNow(newWeightKg);

        double heightM = user.getHeight() / 100.0;
        existing.setBmi(Math.round((newWeightKg / (heightM * heightM)) * 100.0) / 100.0);
        existing.setDifferenceFromStart(Math.round((newWeightKg - user.getInitialWeight()) * 100.0) / 100.0);
        existing.setDifferenceFromTarget(Math.round((newWeightKg - user.getTargetWeight()) * 100.0) / 100.0);

        if (existing.getLogDate().equals(LocalDate.now())) {
            user.setWeight(newWeightKg);
            userService.updateUser(user.getId(), user);
        }

        return weightLogRepository.save(existing);
    }
}