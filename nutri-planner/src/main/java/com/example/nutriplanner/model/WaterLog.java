package com.example.nutriplanner.model;


import com.example.nutriplanner.constants.Unit;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class WaterLog {
    private Long id;
    private User user;
    private int amountMl;
    private LocalDate logDate;
    private Unit unit;
}
