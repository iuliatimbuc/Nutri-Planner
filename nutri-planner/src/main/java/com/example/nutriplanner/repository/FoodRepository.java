package com.example.nutriplanner.repository;

// FoodRepository.java

import com.example.nutriplanner.model.Food;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FoodRepository {

    private final List<Food> foods = new ArrayList<>();
    private Long nextId = 1L;

    public Food save(Food food) {
        if (food.getId() == null) {
            food.setId(nextId++);
            foods.add(food);
        } else {
            foods.removeIf(f -> f.getId().equals(food.getId()));
            foods.add(food);
        }
        return food;
    }

    public Food findById(Long id) {
        return foods.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Food> findAll() {
        return new ArrayList<>(foods);
    }

    public void deleteById(Long id) {
        foods.removeIf(f -> f.getId().equals(id));
    }
}