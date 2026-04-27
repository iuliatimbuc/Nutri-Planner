package com.example.nutriplanner.service.impl;


import com.example.nutriplanner.model.User;
import com.example.nutriplanner.model.WeightLog;
import com.example.nutriplanner.repository.WeightLogRepository;
import com.example.nutriplanner.service.WeightLogService;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class WeightLogServiceImp implements WeightLogService {
    private final WeightLogRepository weightLogRepository;
    private final UserServiceImp userService;

    public WeightLogServiceImp(WeightLogRepository weightLogRepository, UserServiceImp userService) {
        this.weightLogRepository = weightLogRepository;
        this.userService = userService;
    }

    public WeightLog addWeightLog(Long userId, double weightKg, LocalDate date) {
        User user = this.userService.getUserById(userId);
        WeightLog log = this.weightLogRepository.findByUserAndLogDate(user, date);
        if (log == null) {
            log = new WeightLog();
            log.setUser(user);
            log.setLogDate(date);
        }
        log.setWeightNow(weightKg);
        double heightM = user.getHeight() / 100.0;
        log.setBmi(weightKg / (heightM * heightM));
        WeightLog first = this.weightLogRepository.findFirstByUserOrderByLogDateAsc(user);
        log.setDifferenceFromStart(first != null ? weightKg - first.getWeightNow() : 0.0);
        log.setDifferenceFromTarget(weightKg - user.getTargetWeight());
        if (date.equals(LocalDate.now())) {
            user.setWeight(weightKg);
            this.userService.updateUser(user.getId(), user);
        }
        return this.weightLogRepository.save(log);
    }

    public WeightLog getWeightLogById(Long id) {
        WeightLog weightLog = this.weightLogRepository.findById(id);
        if (weightLog != null) {
            return weightLog;
        } else {
            throw new NoSuchElementException("Weight Log with id " + id + " not found");
        }
    }

    public List<WeightLog> getWeightHistory(Long userId) {
        User user = this.userService.getUserById(userId);
        return this.weightLogRepository.findByUserOrderByLogDateAsc(user);
    }

    public WeightLog updateWeightLog(Long logId, double newWeightKg) {
        WeightLog existing = this.getWeightLogById(logId);
        User user = existing.getUser();
        existing.setWeightNow(newWeightKg);
        double heightM = user.getHeight() / (double)100.0F;
        existing.setBmi(newWeightKg / (heightM * heightM));
        WeightLog first = this.weightLogRepository.findFirstByUserOrderByLogDateAsc(user);
        if (first != null) {
            existing.setDifferenceFromStart(newWeightKg - first.getWeightNow());
        }

        existing.setDifferenceFromTarget(newWeightKg - user.getTargetWeight());
        if (existing.getLogDate().equals(LocalDate.now())) {
            user.setWeight(newWeightKg);
            this.userService.updateUser(user.getId(), user);
        }

        return this.weightLogRepository.save(existing);
    }

}

