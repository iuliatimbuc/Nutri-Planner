package com.example.nutriplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NutriPlannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NutriPlannerApplication.class, args);
    }

}
