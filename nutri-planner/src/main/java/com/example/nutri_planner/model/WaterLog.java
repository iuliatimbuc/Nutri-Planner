package com.example.nutri_planner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "waterlog")
@Getter @Setter
public class WaterLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int amountMl;

    private LocalDateTime timestamp;
    private LocalDate logDate;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
        this.logDate = LocalDate.now();
    }
}