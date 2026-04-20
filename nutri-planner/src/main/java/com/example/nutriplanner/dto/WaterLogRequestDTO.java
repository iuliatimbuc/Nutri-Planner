package com.example.nutriplanner.dto;


import java.time.LocalDate;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WaterLogRequestDTO {
    private int amountMl;
    private LocalDate date;
}

