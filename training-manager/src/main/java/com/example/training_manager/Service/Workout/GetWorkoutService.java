package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.ExerciseDto;
import com.example.training_manager.Dto.Workout.SetDto;
import com.example.training_manager.Dto.Workout.WorkoutDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.ExerciseEntity;
import com.example.training_manager.Model.SetEntity;
import com.example.training_manager.Model.WorkoutEntity;
import com.example.training_manager.Repository.ExerciseRepository;
import com.example.training_manager.Repository.SetRepository;
import com.example.training_manager.Repository.WorkoutRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateToken;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GetWorkoutService {
    private final WorkoutRepository workoutRepository;
    private final ValidateToken validateToken;

    @Autowired
    GetWorkoutService(WorkoutRepository workoutRepository, ValidateToken validateToken) {
        this.workoutRepository = workoutRepository;
        this.validateToken = validateToken;
    }

    public WorkoutDto execute(Long id, String authHeader){
        WorkoutEntity workoutEntity;
        Optional<WorkoutEntity> workoutEntityOptional = workoutRepository.findById(id);
        if (workoutEntityOptional.isPresent()){
            workoutEntity = workoutEntityOptional.get();
        }
        else {
            throw new CustomException.WorkoutNotFoundException("Não foi possível encontrar o treino");
        }
        validateToken.execute(workoutEntity.getCustomerEntity().getId(), authHeader);
        return transformWorkoutEntityIntoWorkoutDto(workoutEntity);
    }

    protected WorkoutDto transformWorkoutEntityIntoWorkoutDto(WorkoutEntity workoutEntity){
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setId(workoutEntity.getId());
        workoutDto.setName(workoutEntity.getName());
        workoutDto.setCustomerId(workoutEntity.getCustomerEntity().getId());
        workoutDto.setProgramId(workoutEntity.getProgramEntity().getId());
        workoutDto.setExerciseDtoList(transformExerciseEntityIntoWorkoutDto(workoutEntity.getExerciseEntityList()));
        return workoutDto;
    }

    private List<ExerciseDto> transformExerciseEntityIntoWorkoutDto(List<ExerciseEntity> exerciseEntityList){
        List<ExerciseDto> exerciseDtoList = new ArrayList<>();
        for (int i = 0; i<exerciseEntityList.size(); i++){
            ExerciseDto exerciseDto = new ExerciseDto();
            exerciseDto.setId(exerciseEntityList.get(i).getId());
            exerciseDto.setName(exerciseEntityList.get(i).getName());
            exerciseDto.setSetDtoList(transformSetEntityIntoSetDto(exerciseEntityList.get(i).getSetEntity()));
            exerciseDtoList.add(exerciseDto);
        }
        return exerciseDtoList;
    }

    private List<SetDto> transformSetEntityIntoSetDto(List<SetEntity> setEntityList){
        List<SetDto> setDtoList = new ArrayList<>();
        for (int i = 0; i<setEntityList.size(); i++){
            SetDto setDto = new SetDto();
            setDto.setId(setEntityList.get(i).getId());
            setDto.setRepetitions(setEntityList.get(i).getRepetitions());
            setDto.setWeight(setEntityList.get(i).getWeight());
            setDtoList.add(setDto);
        }
        return setDtoList;
    }
}
