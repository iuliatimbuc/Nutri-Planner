package com.example.nutriplanner.mapper;

import com.example.nutriplanner.dto.UserCreationDTO;
import com.example.nutriplanner.dto.UserDTO;
import com.example.nutriplanner.model.User;
public class UserMapper {

    // GET - trimiti la client
    public static UserDTO toDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .gender(user.getGender())
                .weight(user.getWeight())
                .initialWeight(user.getInitialWeight())
                .height(user.getHeight())
                .targetWeight(user.getTargetWeight())
                .targetDate(user.getTargetDate())
                .goal(user.getGoal())
                .activityLevel(user.getActivityLevel())
                .dailyCalorieGoal(user.getDailyCalorieGoal())
                .dailyProteinGoal(user.getDailyProteinGoal())
                .dailyCarbsGoal(user.getDailyCarbsGoal())
                .dailyFatGoal(user.getDailyFatGoal())
                .build();
    }

    // POST - creare user
    public static User toCreationEntity(UserCreationDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setAge(dto.getAge());
        user.setGender(dto.getGender());
        user.setWeight(dto.getWeight());
        user.setHeight(dto.getHeight());
        user.setTargetWeight(dto.getTargetWeight());
        user.setTargetDate(dto.getTargetDate());
        user.setGoal(dto.getGoal());
        user.setActivityLevel(dto.getActivityLevel());
        return user;
    }
}