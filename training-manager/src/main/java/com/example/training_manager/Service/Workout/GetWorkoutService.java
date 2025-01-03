package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.ExerciseDto;
import com.example.training_manager.Dto.Workout.SetDto;
import com.example.training_manager.Dto.Workout.WorkoutDto;
import com.example.training_manager.Model.ExerciseEntity;
import com.example.training_manager.Model.SetEntity;
import com.example.training_manager.Model.WorkoutEntity;
import com.example.training_manager.Repository.ExerciseRepository;
import com.example.training_manager.Repository.SetRepository;
import com.example.training_manager.Repository.WorkoutRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GetWorkoutService {
    private final SetRepository setRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;

    @Autowired
    GetWorkoutService(SetRepository setRepository,
                      ExerciseRepository exerciseRepository,
                      WorkoutRepository workoutRepository,
                      ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer) {
        this.setRepository = setRepository;
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
    }

    public WorkoutDto execute(Long id, String authHeader) throws Exception{
        WorkoutEntity workoutEntity;
        Optional<WorkoutEntity> workoutEntityOptional = workoutRepository.findById(id);
        if (workoutEntityOptional.isPresent()){
            workoutEntity = workoutEntityOptional.get();
        }
        else {
            throw new Exception("Erro ao encontrar treino");
        }
        if(!validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader), workoutEntity.getCustomerEntity().getId()))
        {
            throw new Exception("O treinador não possui permissão para este cliente");
        }
        return transformWorkoutEntityIntoWorkoutDto(workoutEntity);
    }

    private WorkoutDto transformWorkoutEntityIntoWorkoutDto(WorkoutEntity workoutEntity){
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setId(workoutEntity.getId());
        workoutDto.setName(workoutEntity.getName());
        workoutDto.setProgramId(workoutEntity.getProgramEntity().getId());
        workoutDto.setExercises(transformExerciseEntityIntoWorkoutDto(workoutEntity.getExerciseEntityList()));
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
