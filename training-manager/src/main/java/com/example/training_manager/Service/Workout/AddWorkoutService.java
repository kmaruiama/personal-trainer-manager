package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.ExerciseDto;
import com.example.training_manager.Dto.Workout.SetDto;
import com.example.training_manager.Dto.Workout.WorkoutDto;
import com.example.training_manager.Model.*;
import com.example.training_manager.Repository.*;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
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
    @Autowired
    AddWorkoutService(CustomerRepository customerRepository,
                      ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer,
                      ProgramRepository programRepository,
                      WorkoutRepository workoutRepository,
                      ExerciseRepository exerciseRepository,
                      SetRepository setRepository){
        this.customerRepository = customerRepository;
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.programRepository = programRepository;
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.setRepository = setRepository;
    }

    public void execute (WorkoutDto workoutDto, String authHeader) throws Exception {
        CustomerEntity customerEntity;

        //verificando se o cliente ao qual o treino pertence existe
        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(workoutDto.getCustomerId());
        if (optionalCustomerEntity.isPresent()) {
            customerEntity = optionalCustomerEntity.get();
        } else {
            throw new Exception("Erro ao encontrar o cliente");
        }

        //validacao de segurança
        if (!validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader), customerEntity.getId())) {
            throw new Exception("O treinador não possui permissão para este cliente.");
        }
        addWorkout(workoutDto, customerEntity);
    }

    private void addWorkout(WorkoutDto workoutDto, CustomerEntity customerEntity) throws Exception{

        //inicializando a entidade do planejamento de apenas um treino
        WorkoutEntity workoutEntity = new WorkoutEntity();
        workoutEntity.setName(workoutDto.getName());
        workoutEntity.setCustomerEntity(customerEntity);

        //buscando o id de qual programa o treino pertence
        Optional <ProgramEntity> programEntityOptional = programRepository.findById(workoutDto.getProgramId());
        if (programEntityOptional.isPresent()){
            workoutEntity.setProgramEntity(programEntityOptional.get());
        }
        else {
            throw new Exception("Erro ao encontrar o programa de treinos");
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