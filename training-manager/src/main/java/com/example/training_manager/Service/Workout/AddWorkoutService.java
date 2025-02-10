package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.ExerciseDto;
import com.example.training_manager.Dto.Workout.SetDto;
import com.example.training_manager.Dto.Workout.WorkoutDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.*;
import com.example.training_manager.Repository.*;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateToken;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddWorkoutService{

    private final CustomerRepository customerRepository;
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final ProgramRepository programRepository;
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final SetRepository setRepository;
    private final ValidateToken validateToken;

    @Autowired
    AddWorkoutService(CustomerRepository customerRepository,
                      ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer,
                      ProgramRepository programRepository,
                      WorkoutRepository workoutRepository,
                      ExerciseRepository exerciseRepository,
                      SetRepository setRepository, ValidateToken validateToken){
        this.customerRepository = customerRepository;
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.programRepository = programRepository;
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.setRepository = setRepository;
        this.validateToken = validateToken;
    }

    @Transactional
    public void execute (WorkoutDto workoutDto, String authHeader){
        validateToken.execute(workoutDto.getCustomerId(), authHeader);
        CustomerEntity customerEntity;

        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(workoutDto.getCustomerId());
        if (optionalCustomerEntity.isPresent()) {
            customerEntity = optionalCustomerEntity.get();
        } else {
            throw new CustomException.CustomerNotFound("Cliente não encontrado.");
        }

        addWorkout(workoutDto, customerEntity);
    }

    private void addWorkout(WorkoutDto workoutDto, CustomerEntity customerEntity){

        //inicializando a entidade do planejamento de apenas um treino
        WorkoutEntity workoutEntity = new WorkoutEntity();
        workoutEntity.setName(workoutDto.getName());
        workoutEntity.setCustomerEntity(customerEntity);

        if (workoutDto.isBlueprint()) {
            //buscando o id de qual programa o treino pertence
            Optional<ProgramEntity> programEntityOptional = programRepository.findById(workoutDto.getProgramId());
            if (programEntityOptional.isPresent()) {
                workoutEntity.setProgramEntity(programEntityOptional.get());
            } else {
                throw new CustomException.ProgramNotFoundException("Programa não encontrado");
            }
        }
        //salvando
        workoutRepository.save(workoutEntity);

        //saindo da raiz da arvore
        addExercise(workoutDto.getExerciseDtoList(), workoutEntity);
    }

    protected void addExercise(List<ExerciseDto> exerciseDtoList, WorkoutEntity workoutEntity){
        for (int i = 0; i<exerciseDtoList.size(); i++) {
            ExerciseEntity exerciseEntity = new ExerciseEntity();
            exerciseEntity.setWorkoutEntity(workoutEntity);
            exerciseEntity.setName(exerciseDtoList.get(i).getName());
            exerciseRepository.save(exerciseEntity);
            //subindo para as folhas da arvore
            addSet(exerciseDtoList.get(i).getSetDtoList(), exerciseEntity, workoutEntity.getCustomerEntity());
        }
    }

    protected void addSet(List<SetDto> setDtoList, ExerciseEntity exerciseEntity, CustomerEntity customerEntity){
        for (int i = 0; i<setDtoList.size(); i++){
            SetEntity setEntity = new SetEntity();
            setEntity.setExerciseEntity(exerciseEntity);
            setEntity.setCustomerEntity(customerEntity);
            setEntity.setRepetitions(setDtoList.get(i).getRepetitions());
            setEntity.setWeight(setDtoList.get(i).getWeight());
            setRepository.save(setEntity);
        }
    }
}