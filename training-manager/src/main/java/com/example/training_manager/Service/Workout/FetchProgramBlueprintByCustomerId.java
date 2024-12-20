package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.*;
import com.example.training_manager.Model.ExerciseEntity;
import com.example.training_manager.Model.ProgramEntity;
import com.example.training_manager.Model.SetEntity;
import com.example.training_manager.Model.WorkoutEntity;
import com.example.training_manager.Repository.ExerciseRepository;
import com.example.training_manager.Repository.ProgramRepository;
import com.example.training_manager.Repository.SetRepository;
import com.example.training_manager.Repository.WorkoutRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FetchProgramBlueprintByCustomerId {
    private final ProgramRepository programRepository;
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final SetRepository setRepository;

    @Autowired
    FetchProgramBlueprintByCustomerId(ProgramRepository programRepository,
                                      WorkoutRepository workoutRepository,
                                      ExerciseRepository exerciseRepository,
                                      ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer, SetRepository setRepository) {
        this.programRepository = programRepository;
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
        this.setRepository = setRepository;
    }

    public ProgramBlueprintGetDto execute(Long id, String authHeader) throws Exception {
        if (! validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader), id)){
            throw new Exception("O treinador não possui permissão para este cliente.");
        }
        ProgramEntity programEntity = programRepository.findByCustomerIdAndBlueprintTrue(id);
        ProgramBlueprintGetDto programBlueprintGetDto = new ProgramBlueprintGetDto();
        programBlueprintGetDto.setName(programEntity.getName());
        programBlueprintGetDto.setId(programEntity.getId());
        programBlueprintGetDto.setWorkoutGetDtoList(getWorkouts(programEntity.getId()));
        return programBlueprintGetDto;
    }

    private List<WorkoutGetDto> getWorkouts(Long id){
        List<WorkoutEntity> workouts = workoutRepository.returnAllWorkoutBlueprintsFromProgramById(id);
        List<WorkoutGetDto> workoutGetDtoList = new ArrayList<>();
        for (int i = 0; i<workouts.size(); i++){
            WorkoutGetDto workoutGetDto = new WorkoutGetDto();
            workoutGetDto.setName(workouts.get(i).getName());
            workoutGetDto.setWorkoutId(workouts.get(i).getId());
            workoutGetDto.setExerciseGetDtoList(getExercises(workouts.get(i).getId()));
            workoutGetDtoList.add(workoutGetDto);
        }
        return workoutGetDtoList;
    }

    private List<ExerciseGetDto> getExercises(Long id){
        List<ExerciseEntity> exercises = exerciseRepository.returnAllExerciseBlueprintsFromWorkoutId(id);
        List<ExerciseGetDto> exerciseGetDtoList = new ArrayList<>();
        for (int i = 0; i<exercises.size(); i++){
            ExerciseGetDto exerciseGetDto = new ExerciseGetDto();
            exerciseGetDto.setName(exercises.get(i).getName());
            exerciseGetDto.setId(exercises.get(i).getId());
            exerciseGetDto.setSetGetDtoList(getSets(exercises.get(i).getId()));
            exerciseGetDtoList.add(exerciseGetDto);
        }
        return exerciseGetDtoList;
    }

    private List<SetGetDto> getSets(Long id){
        List<SetEntity> sets = setRepository.returnAllSetsFromExerciseId(id);
        List<SetGetDto> setGetDtoList = new ArrayList<>();
        for (int i = 0; i<sets.size(); i++){
            SetGetDto setGetDto = new SetGetDto();
            setGetDto.setRepetitions(sets.get(i).getRepetitions());
            setGetDto.setId(sets.get(i).getId());
            setGetDto.setWeight(sets.get(i).getWeight());
            setGetDtoList.add(setGetDto);
        }
        return setGetDtoList;
    }
}
