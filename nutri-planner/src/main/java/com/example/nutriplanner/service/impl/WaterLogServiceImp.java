package com.example.nutriplanner.service.impl;


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

    public WaterLog addWaterLog(Long userId, int amountMl, LocalDate date) {
        if (amountMl <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        } else {
            User user = this.userService.getUserById(userId);
            WaterLog log = new WaterLog();
            log.setUser(user);
            log.setAmountMl(amountMl);
            log.setLogDate(date != null ? date : LocalDate.now());
            return this.waterLogsRepository.save(log);
        }
    }

    public WaterLog getWaterLogById(Long id) {
        WaterLog waterLog = this.waterLogsRepository.findById(id);
        if (waterLog != null) {
            return waterLog;
        } else {
            throw new NoSuchElementException("WaterLog with id " + id + " not found");
        }
    }

    public int getTodayTotal(Long userId) {
        User user = this.userService.getUserById(userId);
        Integer total = this.waterLogsRepository.sumAmountByUserAndLogDate(user, LocalDate.now());
        return total != null ? total : 0;
    }

    public List<LocalDate> getLogHistory(Long userId) {
        User user = this.userService.getUserById(userId);
        return this.waterLogsRepository.findDistinctLogDatesByUser(user);
    }

    public int getTotalByDate(Long userId, LocalDate date) {
        User user = this.userService.getUserById(userId);
        Integer total = this.waterLogsRepository.sumAmountByUserAndLogDate(user, date);
        return total != null ? total : 0;
    }

    public WaterLog updateWaterLog(Long logId, int newAmountMl) {
        if (newAmountMl <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        } else {
            WaterLog existing = this.getWaterLogById(logId);
            existing.setAmountMl(newAmountMl);
            return this.waterLogsRepository.save(existing);
        }
    }

    public void deleteWaterLog(Long logId) {
        this.waterLogsRepository.deleteById(logId);
    }
}

