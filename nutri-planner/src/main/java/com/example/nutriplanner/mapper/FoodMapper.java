package com.example.nutriplanner.mapper;

import com.example.nutriplanner.dto.FoodCreationDTO;
import com.example.nutriplanner.dto.FoodDTO;
import com.example.nutriplanner.model.Food;

public class FoodMapper {

    // GET - trimit
    public static FoodDTO toDto(Food food) {
        return FoodDTO.builder()
                .id(food.getId())
                .name(food.getName())
                .calories(food.getCalories())
                .protein(food.getProtein())
                .carbs(food.getCarbs())
                .fat(food.getFat())
                .servingSize(food.getServingSize())
                .unit(food.getUnit())
                .build();
    }

    // POST - creare
    public static Food toCreationEntity(FoodCreationDTO dto) {
        Food food = new Food();
        food.setName(dto.getName());
        food.setCalories(dto.getCalories());
        food.setProtein(dto.getProtein());
        food.setCarbs(dto.getCarbs());
        food.setFat(dto.getFat());
        food.setServingSize(dto.getServingSize());
        food.setUnit(dto.getUnit());
        return food;
    }
}