package com.example.nutriplanner.service.impl;

import com.example.nutriplanner.exceptions.ApiExceptionResponse;
import com.example.nutriplanner.model.Food;
import com.example.nutriplanner.repository.FoodRepository;
import com.example.nutriplanner.service.FoodService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FoodServiceImp implements FoodService {

    private final FoodRepository foodRepository;

    public FoodServiceImp(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public Food createFood(Food food) {
        return foodRepository.save(food);
    }

    public List<Food> getAllFoods() {
        List<Food> foods = new ArrayList<>();
        foodRepository.findAll().forEach(foods::add);
        return foods;
    }

    public Food getFoodById(Long id) throws ApiExceptionResponse{
        Food food = foodRepository.findById(id).orElse(null);
        if (food == null) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("No food with id " + id))
                    .message("Entity not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        return food;
    }

    public Food updateFood(Long id, Food updatedFood) throws ApiExceptionResponse{
        Food existing = getFoodById(id);
        existing.setName(updatedFood.getName());
        existing.setCalories(updatedFood.getCalories());
        existing.setProtein(updatedFood.getProtein());
        existing.setCarbs(updatedFood.getCarbs());
        existing.setFat(updatedFood.getFat());
        existing.setUnit(updatedFood.getUnit());
        existing.setServingSize(updatedFood.getServingSize());
        return foodRepository.save(existing);
    }

    public void deleteFood(Long id) {
        foodRepository.deleteById(id);
    }
}