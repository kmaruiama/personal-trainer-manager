package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.DeleteWorkoutDto;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.ExerciseEntity;
import com.example.training_manager.Model.SetEntity;
import com.example.training_manager.Model.WorkoutEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.ExerciseRepository;
import com.example.training_manager.Repository.SetRepository;
import com.example.training_manager.Repository.WorkoutRepository;
import com.example.training_manager.Service.Shared.ValidateToken;
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

    public void execute (DeleteWorkoutDto deleteWorkoutDto, String authHeader) throws Exception{
        validateToken.execute(deleteWorkoutDto.getCustomerId(), authHeader);
        switch (deleteWorkoutDto.getTreeDeletionLevel()) {
            case 1 -> deleteWorkout(deleteWorkoutDto);
            case 2 -> deleteExercise(deleteWorkoutDto);
            case 3 -> deleteSet(deleteWorkoutDto);
            default -> throw new IllegalArgumentException("Nível inválido");
        }
    }

    private void deleteSet(DeleteWorkoutDto deleteWorkoutDto) throws Exception{
        initializeCustomer(deleteWorkoutDto);
        initializeWorkout(deleteWorkoutDto);
        initializeExercise(deleteWorkoutDto);
        initializeSet(deleteWorkoutDto);
        if(checkSetDeleteCondition()){
            setRepository.delete(this.setEntity);
        }
    }

    private void deleteExercise(DeleteWorkoutDto deleteWorkoutDto) throws Exception{
        initializeCustomer(deleteWorkoutDto);
        initializeWorkout(deleteWorkoutDto);
        initializeExercise(deleteWorkoutDto);
        if(checkExerciseDeleteCondition()){
            exerciseRepository.delete(this.exerciseEntity);
        }
    }

    private void deleteWorkout(DeleteWorkoutDto deleteWorkoutDto) throws Exception{
        initializeCustomer(deleteWorkoutDto);
        initializeWorkout(deleteWorkoutDto);
        if(checkWorkoutDeleteCondition()){
            workoutRepository.delete(this.workoutEntity);
        }
    }

    private void initializeCustomer(DeleteWorkoutDto deleteWorkoutDto) throws Exception {
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(deleteWorkoutDto.getCustomerId());
        if (customerEntityOptional.isPresent()) {
            this.customerEntity = customerEntityOptional.get();
        } else {
            throw new Exception("Erro ao encontrar cliente na base de dados");
        }
    }

    private void initializeWorkout(DeleteWorkoutDto deleteWorkoutDto) throws Exception {
        Optional<WorkoutEntity> workoutEntityOptional = workoutRepository.findById(deleteWorkoutDto.getWorkoutId());
        if (workoutEntityOptional.isPresent()) {
            this.workoutEntity = workoutEntityOptional.get();
        } else {
            throw new Exception("Erro ao encontrar treino");
        }
    }

    private void initializeExercise(DeleteWorkoutDto deleteWorkoutDto) throws Exception {
        Optional<ExerciseEntity> exerciseEntityOptional = exerciseRepository.findById(deleteWorkoutDto.getExerciseId());
        if (exerciseEntityOptional.isPresent()) {
            this.exerciseEntity = exerciseEntityOptional.get();
        } else {
            throw new Exception("Erro ao encontrar exercício");
        }
    }

    private void initializeSet(DeleteWorkoutDto deleteWorkoutDto) throws Exception {
        Optional<SetEntity> setEntityOptional = setRepository.findById(deleteWorkoutDto.getSetId());
        if (setEntityOptional.isPresent()) {
            this.setEntity = setEntityOptional.get();
        } else {
            throw new Exception("Erro ao encontrar série");
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