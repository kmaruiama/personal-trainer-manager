package com.example.training_manager.Model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="workout_blueprint")
public class WorkoutEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private CustomerEntity customerEntity;

    @OneToMany(mappedBy = "workoutEntity", cascade = CascadeType.MERGE)
    private List<ExerciseEntity> exerciseEntityList = new ArrayList<>();

    //???????????
    @OneToMany(mappedBy = "id", cascade = CascadeType.REMOVE)
    private List<ScheduleEntity> schedule = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private ProgramEntity programEntity;
}
