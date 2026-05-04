package com.example.nutriplanner.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class WeightLogDTO {

    private Long id;

    @NotNull(message = "User is required")
    private Long userId;

    @Positive(message = "Weight must be positive")
    @DecimalMin(value = "20.0", message = "Weight cannot be less than 20kg")
    @DecimalMax(value = "500.0", message = "Weight cannot exceed 500kg")
    private double weightNow;

    @PastOrPresent(message = "Log date cannot be in the future")
    private LocalDate logDate;

    private double bmi;
    private double differenceFromTarget;
    private double differenceFromStart;
}