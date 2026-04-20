package com.example.nutri_planner.service;

import com.example.nutri_planner.model.User;
import com.example.nutri_planner.model.WaterLog;
import com.example.nutri_planner.repository.WaterLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class WaterLogService {

    @Autowired
    private WaterLogRepository waterLogsRepository;

    @Autowired
    private UserService userService;

    // inregistrare noua de consum de apa
    public WaterLog addWaterLog(Long userId, int amountMl) {
        if (amountMl <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }
        User user = userService.getUserById(userId);

        WaterLog log = new WaterLog();
        log.setUser(user);
        log.setAmountMl(amountMl);

        return waterLogsRepository.save(log);
    }

    // cauta o inregistrare dupa un id unic
    public WaterLog getWaterLogById(Long id) {
        return waterLogsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WaterLog with id " + id + " not found"));
    }

    // de cate ori am adaugat apa azi
    public List<WaterLog> getTodayLogs(Long userId) {
        User user = userService.getUserById(userId);
        return waterLogsRepository.findByUserAndLogDate(user, LocalDate.now());
    }

    // apa bauta dintr-o zi specifica
    public List<WaterLog> getLogsByDate(Long userId, LocalDate date) {
        User user = userService.getUserById(userId);
        return waterLogsRepository.findByUserAndLogDate(user, date);
    }

    // totalul de azi in ml
    public int getTodayTotal(Long userId) {
        User user = userService.getUserById(userId);
        Integer total = waterLogsRepository.sumAmountByUserAndLogDate(user, LocalDate.now());
        return total != null ? total : 0;
    }

    // toate zilele din istoric in care utilizatorul a baut apa
    public List<LocalDate> getLogHistory(Long userId) {
        User user = userService.getUserById(userId);
        return waterLogsRepository.findDistinctLogDatesByUser(user);
    }


    public WaterLog updateWaterLog(Long logId, int newAmountMl) {
        if (newAmountMl <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }
        WaterLog existing = getWaterLogById(logId);
        existing.setAmountMl(newAmountMl);
        return waterLogsRepository.save(existing);
    }

    public void deleteWaterLog(Long logId) {
        if (!waterLogsRepository.existsById(logId)) {
            throw new RuntimeException("Water with id " + logId + " not found");
        }
        waterLogsRepository.deleteById(logId);
    }
}