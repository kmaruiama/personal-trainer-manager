package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.ExerciseGetDto;
import com.example.training_manager.Dto.Workout.ProgramBlueprintGetDto;
import com.example.training_manager.Dto.Workout.SetGetDto;
import com.example.training_manager.Dto.Workout.WorkoutGetDto;
import com.example.training_manager.Model.*;
import com.example.training_manager.Repository.ExerciseRepository;
import com.example.training_manager.Repository.ProgramRepository;
import com.example.training_manager.Repository.SetRepository;
import com.example.training_manager.Repository.WorkoutRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class EditProgramService {

    private final ProgramRepository programRepository;
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;
    private final SetRepository setRepository;

    @Autowired
    public EditProgramService(ProgramRepository programRepository,
                              ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer,
                              ExerciseRepository exerciseRepository,
                              WorkoutRepository workoutRepository,
                              SetRepository setRepository) {
        this.programRepository = programRepository;
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
        this.setRepository = setRepository;
    }

    public void execute(ProgramBlueprintGetDto programBlueprintGetDto, String authHeader) throws Exception {
        if (!validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader),
                programRepository.returnCustomerIdByProgramBlueprintId(programBlueprintGetDto.getId()))) {
            throw new Exception("O treinador não possui permissão para este cliente.");
        }

        Optional<ProgramEntity> programBlueprintEntityOptional = programRepository.findById(programBlueprintGetDto.getId());
        if (programBlueprintEntityOptional.isEmpty()) {
            throw new Exception("Erro ao encontrar programa de treinos");
        }
        ProgramEntity programEntity = programBlueprintEntityOptional.get();
        programEntity.setName(programBlueprintGetDto.getName());

        editWorkouts(programBlueprintGetDto);
    }

    //modificar esse metodo depois pra nao iterar sobre o for do programa mas tratar cada treino de
    //forma individual, e jogar o laço pro execute()

    private void editWorkouts(ProgramBlueprintGetDto programBlueprintGetDto) throws Exception {
        for (WorkoutGetDto workoutGetDto : programBlueprintGetDto.getWorkoutGetDtoList()) {
            Optional<WorkoutEntity> workoutBlueprintEntityOptional = workoutRepository.findById(workoutGetDto.getWorkoutId());
            if (workoutBlueprintEntityOptional.isEmpty()) {
                throw new Exception("Erro ao encontrar treino do programa");
            }
            WorkoutEntity workoutEntity = workoutBlueprintEntityOptional.get();
            if (workoutGetDto.isDeleteFlag()) {
                workoutRepository.delete(workoutEntity);
            } else {
                workoutEntity.setName(workoutGetDto.getName());
                editExercises(workoutGetDto, workoutEntity.getCustomerEntity());
                workoutRepository.save(workoutEntity);
            }
        }
    }

    private void editExercises(WorkoutGetDto workoutGetDto, CustomerEntity customerEntity) throws Exception {
        for (ExerciseGetDto exerciseGetDto : workoutGetDto.getExerciseGetDtoList()) {
            Optional<ExerciseEntity> exerciseBlueprintEntityOptional = exerciseRepository.findById(exerciseGetDto.getId());
            if (exerciseBlueprintEntityOptional.isEmpty()) {
                throw new Exception("Erro ao encontrar exercícios do treino");
            }
            ExerciseEntity exerciseEntity = exerciseBlueprintEntityOptional.get();

            if (exerciseGetDto.isDeleteFlag()) {
                exerciseRepository.delete(exerciseEntity);
            } else {
                exerciseEntity.setName(exerciseGetDto.getName());
                exerciseRepository.save(exerciseEntity);
                editSets(exerciseGetDto, customerEntity, exerciseEntity);
            }
        }
    }

    private void editSets(ExerciseGetDto exerciseGetDto, CustomerEntity customerEntity, ExerciseEntity exerciseEntity) throws Exception {
        for (SetGetDto setGetDto : exerciseGetDto.getSetGetDtoList()) {
            if (setGetDto.getId() == -1 && !setGetDto.isDeleteFlag()){
                SetEntity setEntity = new SetEntity();
                setEntity.setWeight(setGetDto.getWeight());
                setEntity.setRepetitions(setGetDto.getRepetitions());
                setEntity.setCustomerEntity(customerEntity);
                setEntity.setExerciseEntity(exerciseEntity);
                setRepository.save(setEntity);
                break;
            }

            Optional<SetEntity> setEntityOptional = setRepository.findById(setGetDto.getId());
            if (setEntityOptional.isEmpty()) {
                throw new Exception("Erro ao encontrar séries do treino");
            }

            SetEntity existingSetEntity = setEntityOptional.get();

            if (setGetDto.isDeleteFlag()) {
                setRepository.delete(existingSetEntity);
            } else {
                existingSetEntity.setWeight(setGetDto.getWeight());
                existingSetEntity.setRepetitions(setGetDto.getRepetitions());
                setRepository.save(existingSetEntity);
            }
        }
    }
}
