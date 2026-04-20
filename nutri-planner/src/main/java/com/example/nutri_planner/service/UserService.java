package com.example.nutri_planner.service;

import com.example.nutri_planner.model.Gender;
import com.example.nutri_planner.model.User;
import com.example.nutri_planner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));
    }


    public User updateUser(Long id, User updatedUser) {
        User existing = getUserById(id);

        existing.setName(updatedUser.getName());
        existing.setAge(updatedUser.getAge());
        existing.setGender(updatedUser.getGender());
        existing.setWeight(updatedUser.getWeight());
        existing.setHeight(updatedUser.getHeight());
        existing.setTargetWeight(updatedUser.getTargetWeight());
        existing.setTargetDate(updatedUser.getTargetDate());
        existing.setGoal(updatedUser.getGoal());
        existing.setActivityLevel(updatedUser.getActivityLevel());

        calculateDailyGoals(existing);

        return userRepository.save(existing);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }


    private void calculateDailyGoals(User user) {

        if (user.getWeight() == 0 || user.getHeight() == 0 ||
                user.getAge() == 0 || user.getGender() == null ||
                user.getGoal() == null || user.getActivityLevel() == null) {
            return;
        }

        double bmr;
        if (user.getGender() == Gender.MALE) {
            bmr = 10 * user.getWeight()
                    + 6.25 * user.getHeight()
                    - 5 * user.getAge()
                    + 5;
        } else {
            bmr = 10 * user.getWeight()
                    + 6.25 * user.getHeight()
                    - 5 * user.getAge()
                    - 161;
        }

        double tdee = switch (user.getActivityLevel()) {
            case SEDENTARY -> bmr * 1.2;
            case LIGHTLY_ACTIVE -> bmr * 1.375;
            case MODERATELY_ACTIVE -> bmr * 1.55;
            case VERY_ACTIVE -> bmr * 1.725;
            case EXTRA_ACTIVE -> bmr * 1.9;
        };

        double calories = switch (user.getGoal()) {
            case LOSE_WEIGHT -> tdee - 400;
            case GAIN_WEIGHT -> tdee + 400;
            case GAIN_MUSCLE -> tdee + 250;
            case MAINTAIN -> tdee;
        };

        double proteinPerKg = switch (user.getGoal()) {
            case LOSE_WEIGHT -> 1.3;
            case GAIN_MUSCLE -> 1.8;
            case GAIN_WEIGHT -> 1.5;
            case MAINTAIN    -> 1.2;
        };

        int protein = (int) Math.round(user.getWeight() * proteinPerKg);

        double fatPct = switch (user.getGoal()) {
            case LOSE_WEIGHT -> 0.25;
            case GAIN_MUSCLE -> 0.25;
            case GAIN_WEIGHT -> 0.30;
            case MAINTAIN    -> 0.28;
        };
        int fat = (int) Math.round((calories * fatPct) / 9);

        int carbs = (int) Math.round((calories - (protein * 5.0) - (fat * 9.0)) / 4);

        if (carbs < 100) carbs = 100;

        user.setDailyCalorieGoal((int) Math.round(calories));
        user.setDailyProteinGoal(protein);
        user.setDailyCarbsGoal(carbs);
        user.setDailyFatGoal(fat);
    }
}