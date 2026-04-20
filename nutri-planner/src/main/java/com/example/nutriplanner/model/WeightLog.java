package com.example.nutriplanner.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class WeightLog {
    private Long id;
    private User user;
    private double weightNow;
    private LocalDate logDate;
    private double bmi;
    private double differenceFromTarget;
    private double differenceFromStart;
}