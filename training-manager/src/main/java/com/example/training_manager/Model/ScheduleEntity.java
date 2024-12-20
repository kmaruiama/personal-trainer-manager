package com.example.training_manager.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customerEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private TrainerEntity trainerEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    private WorkoutEntity workoutEntity;

    @Column
    private Date dateStart;

    @Column
    private Date dateEnd;

}
