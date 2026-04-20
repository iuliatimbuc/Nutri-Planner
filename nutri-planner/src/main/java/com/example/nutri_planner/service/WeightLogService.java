package com.example.nutri_planner.service;

import com.example.nutri_planner.model.User;
import com.example.nutri_planner.model.WeightLog;
import com.example.nutri_planner.repository.WeightLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class WeightLogService {

    @Autowired
    private WeightLogRepository weightLogRepository;

    @Autowired
    private UserService userService;

    public WeightLog addWeightLog(Long userId, double weightKg) {
        User user = userService.getUserById(userId);

        // nu permite doua inregistrari
        if (weightLogRepository.findByUserAndLogDate(user, LocalDate.now()).isPresent()) {
            throw new  RuntimeException("Weight Log already exists for today");
        }

        WeightLog log = new WeightLog();
        log.setUser(user);
        log.setWeightNow(weightKg);
        log.setLogDate(LocalDate.now());

        // calculează BMI
        double heightM = user.getHeight() / 100.0;
        log.setBmi(weightKg / (heightM * heightM));

        // dif fata de start
        weightLogRepository.findFirstByUserOrderByLogDateAsc(user)
                .ifPresent(first -> log.setDifferenceFromStart(weightKg - first.getWeightNow()));

        // dif fata de target
        log.setDifferenceFromTarget(weightKg - user.getTargetWeight());

        return weightLogRepository.save(log);
    }


    public WeightLog getWeightLogById(Long id) {
        return weightLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Weight Log Not Found"));
    }

    // istoricul complet
    public List<WeightLog> getWeightHistory(Long userId) {
        User user = userService.getUserById(userId);
        return weightLogRepository.findByUserOrderByLogDateAsc(user);
    }

    // greutatea curenta
    public WeightLog getCurrentWeight(Long userId) {
        User user = userService.getUserById(userId);
        return weightLogRepository.findFirstByUserOrderByLogDateDesc(user)
                .orElseThrow(() -> new RuntimeException("Weight Log Not Found"));
    }

    // greutatea de start
    public WeightLog getStartWeight(Long userId) {
        User user = userService.getUserById(userId);
        return weightLogRepository.findFirstByUserOrderByLogDateAsc(user)
                .orElseThrow(() -> new RuntimeException("Weight Log Not Found"));
    }

    public WeightLog updateWeightLog(Long logId, double newWeightKg) {
        WeightLog existing = getWeightLogById(logId);
        User user = existing.getUser();

        existing.setWeightNow(newWeightKg);

        double heightM = user.getHeight() / 100.0;
        existing.setBmi(newWeightKg / (heightM * heightM));

        weightLogRepository.findFirstByUserOrderByLogDateAsc(user)
                .ifPresent(first -> existing.setDifferenceFromStart(newWeightKg - first.getWeightNow()));

        existing.setDifferenceFromTarget(newWeightKg - user.getTargetWeight());

        return weightLogRepository.save(existing);
    }

    public void deleteWeightLog(Long logId) {
        if (!weightLogRepository.existsById(logId)) {
            throw new RuntimeException("Weight Log Not Found");
        }
        weightLogRepository.deleteById(logId);
    }
}
