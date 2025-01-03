package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.ExerciseDto;
import com.example.training_manager.Model.ExerciseEntity;
import com.example.training_manager.Model.WorkoutEntity;
import com.example.training_manager.Repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final AddSetService addSetService;

    @Autowired
    AddExerciseService(ExerciseRepository exerciseRepository, AddSetService addSetService){
        this.exerciseRepository = exerciseRepository;
        this.addSetService = addSetService;
    }
    protected void execute(List<ExerciseDto> exerciseDtoList, WorkoutEntity workoutEntity){
        //nao vou validar o ownership, se ele chegou até aqui já está validado
        List<ExerciseEntity> exerciseEntityList = new ArrayList<>();

        //para cada nó da arvore (exercicio), criamos uma entidade
        for (int i = 0; i<exerciseDtoList.size(); i++) {
            ExerciseEntity exerciseEntity = new ExerciseEntity();
            exerciseEntity.setWorkoutEntity(workoutEntity);
            exerciseEntity.setName(exerciseDtoList.get(i).getName());
            exerciseRepository.save(exerciseEntity);
            //subindo para as folhas da arvore
            addSetService.execute(exerciseDtoList.get(i).getSetDtoList(), exerciseEntity, workoutEntity.getCustomerEntity());
        }
    }
}
