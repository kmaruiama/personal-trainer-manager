package com.example.training_manager.Model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "serie")
public class SetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int repetitions;

    private float weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private ExerciseEntity exerciseEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private CustomerEntity customerEntity;
}
