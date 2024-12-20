package com.example.training_manager.Dto.Workout;

import lombok.Data;

import java.util.List;

@Data
public class ExerciseGetDto {
    private Long id;
    private String name;
    private List<SetGetDto> setGetDtoList;
    boolean deleteFlag;
}
