package com.example.nutriplanner.service.impl;


import com.example.nutriplanner.exceptions.ApiExceptionResponse;
import com.example.nutriplanner.model.User;
import com.example.nutriplanner.model.WaterLog;
import com.example.nutriplanner.repository.WaterLogRepository;
import com.example.nutriplanner.service.WaterLogService;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class WaterLogServiceImp implements WaterLogService {
    private final WaterLogRepository waterLogsRepository;
    private final UserServiceImp userService;

    public WaterLogServiceImp(WaterLogRepository waterLogsRepository, UserServiceImp userService) {
        this.waterLogsRepository = waterLogsRepository;
        this.userService = userService;
    }

    public int getTotalByDate(Long userId, LocalDate date) throws ApiExceptionResponse {
        User user = userService.getUserById(userId);
        return waterLogsRepository.findByUserAndLogDate(user, date)
                .stream()
                .mapToInt(WaterLog::getAmountMl)
                .sum();
    }

    public WaterLog addWaterLog(Long userId, int amountMl, LocalDate date) throws ApiExceptionResponse{
            User user = this.userService.getUserById(userId);
            WaterLog log = new WaterLog();
            log.setUser(user);
            log.setAmountMl(amountMl);
            log.setLogDate(date != null ? date : LocalDate.now());
            return this.waterLogsRepository.save(log);

    }

    public WaterLog updateWaterLog(Long userId, int amountMl, LocalDate date) throws ApiExceptionResponse{
        User user = userService.getUserById(userId);
        LocalDate logDate = date != null ? date : LocalDate.now();

        List<WaterLog> existing = waterLogsRepository.findByUserAndLogDate(user, logDate);

        WaterLog log;
        if (!existing.isEmpty()) {
            log = existing.get(0);
            int newAmount = log.getAmountMl() + amountMl;

            if (newAmount <= 0) {
                waterLogsRepository.deleteById(log.getId());
                return null; // sters
            }

            log.setAmountMl(newAmount);
        } else {
            log = new WaterLog();
            log.setUser(user);
            log.setLogDate(logDate);
            log.setAmountMl(Math.max(amountMl, 0));
        }

        return waterLogsRepository.save(log);
    }
}

