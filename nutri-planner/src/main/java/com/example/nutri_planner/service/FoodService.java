package com.example.nutri_planner.service;

import com.example.nutri_planner.model.Food;
import com.example.nutri_planner.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FoodService {

    private FoodRepository foodRepository;

    public Food createFood(Food food) {
        return foodRepository.save(food);
    }

    public Food getFoodById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food Not Found"));
    }

    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    public List<Food> searchFoodByName(String name) {
        return foodRepository.findByName(name);
    }

    public Food updateFood(Long id, Food updatedFood) {
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
        if (!foodRepository.existsById(id)) {
            throw new RuntimeException("Food Not Found");
        }
        foodRepository.deleteById(id);
    }
}