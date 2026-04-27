package com.example.nutriplanner.service;

import com.example.nutriplanner.model.Food;

import java.util.List;

public interface FoodService {
    Food createFood(Food food);
    Food getFoodById(Long id);
    List<Food> getAllFoods();
    Food updateFood(Long id, Food updatedFood);
    void deleteFood(Long id);
}
