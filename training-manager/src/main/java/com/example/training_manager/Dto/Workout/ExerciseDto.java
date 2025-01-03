package com.example.training_manager.Dto.Workout;

import lombok.Data;

import java.util.List;

@Data
public class ExerciseDto {
    private Long id;
    private String name;
    private List<SetDto> setDtoList;
}
