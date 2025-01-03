package com.example.training_manager.Dto.Workout;

import lombok.Data;

@Data
public class SetDto {
    private Long id;
    public int repetitions;
    public float weight;
}
