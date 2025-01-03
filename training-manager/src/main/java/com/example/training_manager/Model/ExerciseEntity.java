package com.example.training_manager.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
//essa entidade é inutil e pode ser removida adicionando duas colunas extras no SetEntity
public class ExerciseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "workout_id")
    private WorkoutEntity workoutEntity;

    @OneToMany(mappedBy = "exerciseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SetEntity> setEntity = new ArrayList<>();
}
