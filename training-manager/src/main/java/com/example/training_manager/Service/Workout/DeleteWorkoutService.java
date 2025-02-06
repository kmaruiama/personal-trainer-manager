package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.DeleteWorkoutDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.ExerciseEntity;
import com.example.training_manager.Model.SetEntity;
import com.example.training_manager.Model.WorkoutEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.ExerciseRepository;
import com.example.training_manager.Repository.SetRepository;
import com.example.training_manager.Repository.WorkoutRepository;
import com.example.training_manager.Service.Shared.ValidateToken;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteWorkoutService {
    private final ValidateToken validateToken;
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final SetRepository setRepository;
    private final CustomerRepository customerRepository;

    private CustomerEntity customerEntity;
    private WorkoutEntity workoutEntity;
    private ExerciseEntity exerciseEntity;
    private SetEntity setEntity;

    @Autowired
    DeleteWorkoutService(ValidateToken validateToken,
                         WorkoutRepository workoutRepository,
                         ExerciseRepository exerciseRepository,
                         SetRepository setRepository, CustomerRepository customerRepository) {
        this.validateToken = validateToken;
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.setRepository = setRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public void execute (DeleteWorkoutDto deleteWorkoutDto, String authHeader){
        validateToken.execute(deleteWorkoutDto.getCustomerId(), authHeader);
        switch (deleteWorkoutDto.getTreeDeletionLevel()) {
            case 1 -> deleteWorkout(deleteWorkoutDto);
            case 2 -> deleteExercise(deleteWorkoutDto);
            case 3 -> deleteSet(deleteWorkoutDto);
            default -> throw new IllegalArgumentException("Nível inválido");
        }
    }

    private void deleteSet(DeleteWorkoutDto deleteWorkoutDto){
        initializeCustomer(deleteWorkoutDto);
        initializeWorkout(deleteWorkoutDto);
        initializeExercise(deleteWorkoutDto);
        initializeSet(deleteWorkoutDto);
        if(checkSetDeleteCondition()){
            setRepository.delete(this.setEntity);
        }
    }

    private void deleteExercise(DeleteWorkoutDto deleteWorkoutDto){
        initializeCustomer(deleteWorkoutDto);
        initializeWorkout(deleteWorkoutDto);
        initializeExercise(deleteWorkoutDto);
        if(checkExerciseDeleteCondition()){
            exerciseRepository.delete(this.exerciseEntity);
        }
    }

    private void deleteWorkout(DeleteWorkoutDto deleteWorkoutDto){
        initializeCustomer(deleteWorkoutDto);
        initializeWorkout(deleteWorkoutDto);
        if(checkWorkoutDeleteCondition()){
            workoutRepository.delete(this.workoutEntity);
        }
    }

    private void initializeCustomer(DeleteWorkoutDto deleteWorkoutDto){
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(deleteWorkoutDto.getCustomerId());
        if (customerEntityOptional.isPresent()) {
            this.customerEntity = customerEntityOptional.get();
        } else {
            throw new CustomException.CustomerNotFound("Cliente não encontrado.");
        }
    }

    private void initializeWorkout(DeleteWorkoutDto deleteWorkoutDto){
        Optional<WorkoutEntity> workoutEntityOptional = workoutRepository.findById(deleteWorkoutDto.getWorkoutId());
        if (workoutEntityOptional.isPresent()) {
            this.workoutEntity = workoutEntityOptional.get();
        } else {
            throw new CustomException.WorkoutNotFoundException("Treino não encontrado");
        }
    }

    private void initializeExercise(DeleteWorkoutDto deleteWorkoutDto){
        Optional<ExerciseEntity> exerciseEntityOptional = exerciseRepository.findById(deleteWorkoutDto.getExerciseId());
        if (exerciseEntityOptional.isPresent()) {
            this.exerciseEntity = exerciseEntityOptional.get();
        } else {
            throw new CustomException.ExerciseNotFoundException("Erro ao encontrar exercício");
        }
    }

    private void initializeSet(DeleteWorkoutDto deleteWorkoutDto){
        Optional<SetEntity> setEntityOptional = setRepository.findById(deleteWorkoutDto.getSetId());
        if (setEntityOptional.isPresent()) {
            this.setEntity = setEntityOptional.get();
        } else {
            throw new CustomException.SetNotFoundException("Erro ao encontrar série");
        }
    }

    private boolean checkWorkoutDeleteCondition (){
        return this.workoutEntity.getCustomerEntity().getId() == this.customerEntity.getId();
    }

    private boolean checkExerciseDeleteCondition (){
        return this.exerciseEntity.getWorkoutEntity().getId() == this.workoutEntity.getId()
                && checkWorkoutDeleteCondition();
    }

    private boolean checkSetDeleteCondition (){
        return this.setEntity.getExerciseEntity().getId() == this.exerciseEntity.getId()
                && checkExerciseDeleteCondition()
                && checkWorkoutDeleteCondition();
    }

}