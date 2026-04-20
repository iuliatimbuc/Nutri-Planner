package com.example.nutri_planner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "weightlog")
@Getter @Setter
public class WeightLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private double weightNow;        
    private LocalDate logDate;

    private double bmi;
    private double differenceFromStart;
    private double differenceFromTarget;
}