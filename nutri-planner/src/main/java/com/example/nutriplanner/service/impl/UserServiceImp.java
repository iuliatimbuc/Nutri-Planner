package com.example.nutriplanner.service.impl;

import com.example.nutriplanner.constants.Gender;
import com.example.nutriplanner.exceptions.ApiExceptionResponse;
import com.example.nutriplanner.model.User;
import com.example.nutriplanner.repository.UserRepository;
import com.example.nutriplanner.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserById(Long id) throws ApiExceptionResponse {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("No user with id " + id))
                    .message("Entity not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        return user;
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setInitialWeight(user.getWeight());
        calculateDailyGoals(user);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public User updateUser(Long id, User updatedUser) throws ApiExceptionResponse {
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
        userRepository.deleteById(id);
    }

    private void calculateDailyGoals(User user) {
        if (user.getWeight() != 0.0 && user.getHeight() != 0.0
                && user.getAge() != 0 && user.getGender() != null
                && user.getGoal() != null && user.getActivityLevel() != null) {

            double bmr;
            if (user.getGender() == Gender.MALE) {
                bmr = 10.0 * user.getWeight() + 6.25 * user.getHeight() - 5 * user.getAge() + 5.0;
            } else {
                bmr = 10.0 * user.getWeight() + 6.25 * user.getHeight() - 5 * user.getAge() - 161.0;
            }

            double tdee = switch (user.getActivityLevel()) {
                case SEDENTARY -> bmr * 1.2;
                case LIGHTLY_ACTIVE -> bmr * 1.375;
                case MODERATELY_ACTIVE -> bmr * 1.55;
                case VERY_ACTIVE -> bmr * 1.725;
                case EXTRA_ACTIVE -> bmr * 1.9;
            };

            double calories = switch (user.getGoal()) {
                case LOSE_WEIGHT -> tdee - 400.0;
                case GAIN_WEIGHT -> tdee + 400.0;
                case GAIN_MUSCLE -> tdee + 250.0;
                case MAINTAIN -> tdee;
            };

            double proteinPerKg = switch (user.getGoal()) {
                case LOSE_WEIGHT -> 1.3;
                case GAIN_WEIGHT -> 1.5;
                case GAIN_MUSCLE -> 1.8;
                case MAINTAIN -> 1.2;
            };

            int protein = (int) Math.round(user.getWeight() * proteinPerKg);

            double fatPct = switch (user.getGoal()) {
                case LOSE_WEIGHT -> 0.25;
                case GAIN_WEIGHT -> 0.30;
                case GAIN_MUSCLE -> 0.25;
                case MAINTAIN -> 0.28;
            };

            int fat = (int) Math.round(calories * fatPct / 9.0);
            int carbs = (int) Math.round((calories - protein * 4.0 - fat * 9.0) / 4.0);
            if (carbs < 100) carbs = 100;

            user.setDailyCalorieGoal((int) Math.round(calories));
            user.setDailyProteinGoal(protein);
            user.setDailyCarbsGoal(carbs);
            user.setDailyFatGoal(fat);
        }
    }
}