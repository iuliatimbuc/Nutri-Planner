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

    public int getTotalByDate(Long userId, LocalDate date) {
        User user = this.userService.getUserById(userId);
        Integer total = this.waterLogsRepository.sumAmountByUserAndLogDate(user, date);
        return total != null ? total : 0;
    }

    public WaterLog addWaterLog(Long userId, int amountMl, LocalDate date) {
            User user = this.userService.getUserById(userId);
            WaterLog log = new WaterLog();
            log.setUser(user);
            log.setAmountMl(amountMl);
            log.setLogDate(date != null ? date : LocalDate.now());
            return this.waterLogsRepository.save(log);

    }


}

