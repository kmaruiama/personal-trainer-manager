package com.example.training_manager.Dto.Workout;

import lombok.Data;

@Data
public class DeleteWorkoutDto {
    Long customerId;
    Long workoutId;
    Long exerciseId;
    Long setId;
    int treeDeletionLevel;
}
