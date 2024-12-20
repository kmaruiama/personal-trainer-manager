package com.example.training_manager.Dto.Workout;

import lombok.Data;

@Data
public class SetGetDto {
    private Long id;
    private float weight;
    private int repetitions;
    boolean deleteFlag;
}
