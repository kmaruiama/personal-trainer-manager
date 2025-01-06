package com.example.training_manager.Dto.Workout;

import lombok.Data;

import java.util.List;

@Data
public class WorkoutDto {
    private Long programId;
    private Long id;
    private String name;
    private Long customerId;
    private List<ExerciseDto> exerciseDtoList;
}
