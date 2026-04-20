package com.example.nutri_planner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "dailylog")
@Getter
@Setter
public class DailyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    private Double quantity;
    private double calculatedCalories;
    private double calculatedProtein;
    private double calculatedCarbs;
    private double calculatedFat;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    private LocalDate logDate;
    @PrePersist
    public void prePersist() {
        this.logDate = LocalDate.now();
    }
}
