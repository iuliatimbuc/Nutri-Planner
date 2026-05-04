package com.example.nutriplanner.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class MinTwoWeeksValidator
        implements ConstraintValidator<MinTwoWeeksFromNow, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) return false;
        return date.isAfter(LocalDate.now().plusWeeks(2));
    }
}