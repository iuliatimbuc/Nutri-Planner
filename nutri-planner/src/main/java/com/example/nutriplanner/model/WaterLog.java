package com.example.nutriplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "water_logs")
@Getter
@Setter
public class WaterLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @PositiveOrZero(message = "Amount must be positive or zero")
    @Max(value = 10000, message = "Amount cannot exceed 10000ml")
    private int amountMl;

    private LocalDate logDate;
}