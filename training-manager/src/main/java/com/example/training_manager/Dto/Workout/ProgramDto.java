package com.example.training_manager.Dto.Workout;
import lombok.Data;

import java.util.List;

@Data
public class ProgramDto {
    private String name;
    private Long customerId;
    private List<WorkoutDto> workouts;
    boolean blueprint;
}
