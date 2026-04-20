package com.example.nutriplanner.service.impl;


import com.example.nutriplanner.constants.Gender;
import com.example.nutriplanner.model.User;
import com.example.nutriplanner.repository.UserRepository;
import com.example.nutriplanner.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserById(Long id) {
        User user = this.userRepository.findById(id);
        if (user != null) {
            return user;
        } else {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
    }

    public User createUser(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setInitialWeight(user.getWeight());
        this.calculateDailyGoals(user);
        return this.userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User updateUser(Long id, User updatedUser) {
        User existing = this.getUserById(id);
        existing.setName(updatedUser.getName());
        existing.setAge(updatedUser.getAge());
        existing.setGender(updatedUser.getGender());
        existing.setWeight(updatedUser.getWeight());
        existing.setHeight(updatedUser.getHeight());
        existing.setTargetWeight(updatedUser.getTargetWeight());
        existing.setTargetDate(updatedUser.getTargetDate());
        existing.setGoal(updatedUser.getGoal());
        existing.setActivityLevel(updatedUser.getActivityLevel());
        this.calculateDailyGoals(existing);
        return this.userRepository.save(existing);
    }

    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public User getUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email);
        if (user == null) {
            throw new NoSuchElementException("User with email " + email + " not found");
        } else {
            return user;
        }
    }

    private void calculateDailyGoals(User user) {
        if (user.getWeight() != (double)0.0F && user.getHeight() != (double)0.0F && user.getAge() != 0 && user.getGender() != null && user.getGoal() != null && user.getActivityLevel() != null) {
            double bmr;
            if (user.getGender() == Gender.MALE) {
                bmr = (double)10.0F * user.getWeight() + (double)6.25F * user.getHeight() - (double)(5 * user.getAge()) + (double)5.0F;
            } else {
                bmr = (double)10.0F * user.getWeight() + (double)6.25F * user.getHeight() - (double)(5 * user.getAge()) - (double)161.0F;
            }

            double var10000;
            switch (user.getActivityLevel()) {
                case SEDENTARY -> var10000 = bmr * 1.2;
                case LIGHTLY_ACTIVE -> var10000 = bmr * (double)1.375F;
                case MODERATELY_ACTIVE -> var10000 = bmr * 1.55;
                case VERY_ACTIVE -> var10000 = bmr * 1.725;
                case EXTRA_ACTIVE -> var10000 = bmr * 1.9;
                default -> throw new IncompatibleClassChangeError();
            }

            double tdee = var10000;
            switch (user.getGoal()) {
                case LOSE_WEIGHT -> var10000 = tdee - (double)400.0F;
                case GAIN_WEIGHT -> var10000 = tdee + (double)400.0F;
                case GAIN_MUSCLE -> var10000 = tdee + (double)250.0F;
                case MAINTAIN -> var10000 = tdee;
                default -> throw new IncompatibleClassChangeError();
            }

            double calories = var10000;
            switch (user.getGoal()) {
                case LOSE_WEIGHT -> var10000 = 1.3;
                case GAIN_WEIGHT -> var10000 = (double)1.5F;
                case GAIN_MUSCLE -> var10000 = 1.8;
                case MAINTAIN -> var10000 = 1.2;
                default -> throw new IncompatibleClassChangeError();
            }

            double proteinPerKg = var10000;
            int protein = (int)Math.round(user.getWeight() * proteinPerKg);
            switch (user.getGoal()) {
                case LOSE_WEIGHT -> var10000 = (double)0.25F;
                case GAIN_WEIGHT -> var10000 = 0.3;
                case GAIN_MUSCLE -> var10000 = (double)0.25F;
                case MAINTAIN -> var10000 = 0.28;
                default -> throw new IncompatibleClassChangeError();
            }

            double fatPct = var10000;
            int fat = (int)Math.round(calories * fatPct / (double)9.0F);
            int carbs = (int)Math.round((calories - (double)protein * (double)5.0F - (double)fat * (double)9.0F) / (double)4.0F);
            if (carbs < 100) {
                carbs = 100;
            }

            user.setDailyCalorieGoal((int)Math.round(calories));
            user.setDailyProteinGoal(protein);
            user.setDailyCarbsGoal(carbs);
            user.setDailyFatGoal(fat);
        }
    }
}

