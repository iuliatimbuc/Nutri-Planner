package com.example.nutriplanner.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class WaterLogDTO {

    private Long id;

    @NotNull(message = "User is required")
    private Long userId;

    @Positive(message = "Amount must be positive")
    @Max(value = 10000, message = "Amount cannot exceed 10000ml")
    private int amountMl;

    @PastOrPresent(message = "Log date cannot be in the future")
    private LocalDate logDate;
}