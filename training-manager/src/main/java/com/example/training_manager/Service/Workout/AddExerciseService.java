package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.ExerciseDto;
import com.example.training_manager.Dto.Workout.SetDto;
import com.example.training_manager.Model.ExerciseEntity;
import com.example.training_manager.Model.WorkoutEntity;
import com.example.training_manager.Repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final AddSetService addSetService;

    @Autowired
    public AddExerciseService(ExerciseRepository exerciseRepository,
                              AddSetService addSetService) {
        this.exerciseRepository = exerciseRepository;
        this.addSetService = addSetService;
    }

    public void execute(ExerciseDto exerciseDto, WorkoutEntity workoutEntity) {
        ExerciseEntity exerciseEntity = new ExerciseEntity();
        exerciseEntity.setWorkoutEntity(workoutEntity);
        exerciseEntity.setName(exerciseDto.getName());
        exerciseRepository.save(exerciseEntity);

        for (SetDto setDto : exerciseDto.getSetDtoList()) {
            addSetService.execute(exerciseEntity, setDto, workoutEntity.getCustomerEntity());
        }
    }

}