package com.example.training_manager.Dto.Workout;

import lombok.Data;

import java.util.List;

@Data
public class WorkoutGetDto {
    private Long workoutId;
    private String name;
    private List<ExerciseGetDto> exerciseGetDtoList;
    private boolean deleteFlag;
}
