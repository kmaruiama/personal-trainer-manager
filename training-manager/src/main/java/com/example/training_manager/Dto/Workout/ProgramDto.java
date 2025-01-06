package com.example.training_manager.Dto.Workout;

import lombok.Data;

import java.util.List;

@Data
public class ProgramDto {
    String name;
    Long id;
    Long customerId;
    List<WorkoutDto> workoutDtoList;
}
