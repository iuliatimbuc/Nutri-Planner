package com.example.nutriplanner.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinTwoWeeksValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinTwoWeeksFromNow {
    String message() default "Target date must be at least 2 weeks from today";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}