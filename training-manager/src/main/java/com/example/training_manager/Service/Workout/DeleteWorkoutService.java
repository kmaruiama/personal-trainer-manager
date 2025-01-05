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

    private CustomerEntity customerEntity;  //como sao multiplos usos, nao vou instanciar em um metodo apenas
    private WorkoutEntity workoutEntity;
    private ExerciseEntity exerciseEntity;
    private SetEntity setEntity;

    @Autowired
    DeleteWorkoutService(ValidateToken validateToken,
                         WorkoutRepository workoutRepository,
                         ExerciseRepository exerciseRepository,
                         SetRepository setRepository, CustomerRepository customerRepository){
        this.validateToken = validateToken;
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.setRepository = setRepository;
        this.customerRepository = customerRepository;
    }

    public void execute(DeleteWorkoutDto deleteWorkoutDto, String authHeader) throws Exception{
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(deleteWorkoutDto.getCustomerId());
        if (customerEntityOptional.isPresent()){
            this.customerEntity = customerEntityOptional.get();
        }
        else {
            throw new Exception("Erro ao encontrar cliente na database");
        }
        validateToken.execute(this.customerEntity.getId(), authHeader);
        redirectToTreeLevel(deleteWorkoutDto);
    }
    private void redirectToTreeLevel(DeleteWorkoutDto deleteWorkoutDto) throws Exception{
        switch (deleteWorkoutDto.getTreeDeletionLevel()){
            case 1: deleteWorkout(deleteWorkoutDto);
            break;

            case 2: deleteExercise(deleteWorkoutDto);
            break;

            case 3: deleteSet(deleteWorkoutDto);
            break;
        }
    }

    private  void deleteWorkout(DeleteWorkoutDto deleteWorkoutDto) throws Exception{
        if (checkWorkoutOwner(deleteWorkoutDto)){
            workoutRepository.delete(this.workoutEntity);
        }
    }

    private void deleteExercise(DeleteWorkoutDto deleteWorkoutDto) throws Exception{
        if (checkExercisesWorkout(deleteWorkoutDto)){
            exerciseRepository.delete(this.exerciseEntity);
        }
    }

    private void deleteSet(DeleteWorkoutDto deleteWorkoutDto) throws Exception{
        if(checkSetsExercise(deleteWorkoutDto)){
            setRepository.delete(this.setEntity);
        }
    }

    //os seguintes metodos necessarios servem para evitar que um treinador apenas com a autenticacao do ownership
    //do cliente consiga deletar qualquer coisa no banco de dados

    private boolean checkWorkoutOwner(DeleteWorkoutDto deleteWorkoutDto) throws Exception {
        Optional<WorkoutEntity> workoutEntityOptional = workoutRepository.findById(deleteWorkoutDto.getWorkoutId());
        if (workoutEntityOptional.isPresent()){
            this.workoutEntity = workoutEntityOptional.get();
        }
        else {
            throw new Exception("Erro ao encontrar treino");
        }

        return this.customerEntity.getId() == this.workoutEntity.getCustomerEntity().getId();
    }

    private boolean checkExercisesWorkout(DeleteWorkoutDto deleteWorkoutDto) throws Exception {
        Optional<WorkoutEntity> workoutEntityOptional = workoutRepository.findById(deleteWorkoutDto.getWorkoutId());
        if (workoutEntityOptional.isPresent()){
            this.workoutEntity = workoutEntityOptional.get();
        }
        else {
            throw new Exception("Erro ao encontrar treino");
        }
        Optional<ExerciseEntity> exerciseEntityOptional = exerciseRepository.findById(deleteWorkoutDto.getExerciseId());
        if (exerciseEntityOptional.isPresent()){
            this.exerciseEntity = exerciseEntityOptional.get();
        }
        else {
            throw new Exception("Erro ao encontrar exercício");
        }
        return this.workoutEntity.getId() == this.exerciseEntity.getWorkoutEntity().getId();
    }

    private boolean checkSetsExercise(DeleteWorkoutDto deleteWorkoutDto) throws Exception {
        Optional<ExerciseEntity> exerciseEntityOptional = exerciseRepository.findById(deleteWorkoutDto.getExerciseId());
        if (exerciseEntityOptional.isPresent()){
            this.exerciseEntity = exerciseEntityOptional.get();
        }
        else {
            throw new Exception("Erro ao encontrar exercício");
        }
        Optional<SetEntity> setEntityOptional = setRepository.findById(deleteWorkoutDto.getSetId());
        if (setEntityOptional.isPresent()){
            this.setEntity = setEntityOptional.get();
        }
        else {
            throw new Exception("Erro ao encontrar série");
        }
        return this.exerciseEntity.getId() == this.setEntity.getExerciseEntity().getId();
    }
}

//preciso aprender generals pra deixar o codigo menos verboso
