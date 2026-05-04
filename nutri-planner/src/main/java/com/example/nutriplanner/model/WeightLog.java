package com.example.nutriplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "weight_logs")
@Getter
@Setter
public class WeightLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Positive(message = "Weight must be positive")
    @DecimalMin(value = "20.0", message = "Weight cannot be less than 20kg")
    @DecimalMax(value = "500.0", message = "Weight cannot exceed 500kg")
    private double weightNow;

    private LocalDate logDate;

    @PositiveOrZero(message = "BMI must be positive")
    @DecimalMin(value = "10.0", message = "BMI cannot be less than 10")
    @DecimalMax(value = "100.0", message = "BMI cannot exceed 100")
    private double bmi;

    private double differenceFromTarget;
    private double differenceFromStart;
}