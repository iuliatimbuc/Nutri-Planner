package com.example.nutriplanner.repository;

import com.example.nutriplanner.model.Food;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodRepository extends CrudRepository<Food, Long> {}