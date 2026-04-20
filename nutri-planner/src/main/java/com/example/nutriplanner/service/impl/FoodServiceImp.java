package com.example.nutriplanner.service.impl;


import com.example.nutriplanner.model.Food;
import com.example.nutriplanner.repository.FoodRepository;
import com.example.nutriplanner.service.FoodService;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class FoodServiceImp implements FoodService {
    private final FoodRepository foodRepository;

    public FoodServiceImp(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public Food createFood(Food food) {
        return this.foodRepository.save(food);
    }

    public List<Food> getAllFoods() {
        return this.foodRepository.findAll();
    }

    public Food getFoodById(Long id) {
        Food food = this.foodRepository.findById(id);
        if (food != null) {
            return food;
        } else {
            throw new NoSuchElementException("Food with id " + id + " not found");
        }
    }

    public Food updateFood(Long id, Food updatedFood) {
        Food existing = this.getFoodById(id);
        existing.setName(updatedFood.getName());
        existing.setCalories(updatedFood.getCalories());
        existing.setProtein(updatedFood.getProtein());
        existing.setCarbs(updatedFood.getCarbs());
        existing.setFat(updatedFood.getFat());
        existing.setUnit(updatedFood.getUnit());
        existing.setServingSize(updatedFood.getServingSize());
        return this.foodRepository.save(existing);
    }

    public void deleteFood(Long id) {
        this.foodRepository.deleteById(id);
    }
}

