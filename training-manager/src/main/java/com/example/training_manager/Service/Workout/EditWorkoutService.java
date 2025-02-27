package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.ExerciseDto;
import com.example.training_manager.Dto.Workout.SetDto;
import com.example.training_manager.Dto.Workout.WorkoutDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.ExerciseEntity;
import com.example.training_manager.Model.SetEntity;
import com.example.training_manager.Model.WorkoutEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.ExerciseRepository;
import com.example.training_manager.Repository.SetRepository;
import com.example.training_manager.Repository.WorkoutRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateToken;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

//provavelmente tem um jeito mais performatico de fazer isso passando especificamente quais
//partes foram editadas, mas ¯\_(ツ)_/¯ preciso terminar esse projeto rapido
@Service
public class EditWorkoutService {
    private final WorkoutRepository workoutRepository;
    private final AddWorkoutService addWorkoutService;
    private final ExerciseRepository exerciseRepository;
    private final CustomerRepository customerRepository;
    private final SetRepository setRepository;
    private final ValidateToken validateToken;
    private CustomerEntity customerEntity;

    @Autowired
    EditWorkoutService(WorkoutRepository workoutRepository,
                       AddWorkoutService addWorkoutService,
                       ExerciseRepository exerciseRepository,
                       CustomerRepository customerRepository,
                       SetRepository setRepository,
                       ValidateToken validateToken){
        this.workoutRepository = workoutRepository;
        this.addWorkoutService = addWorkoutService;
        this.exerciseRepository = exerciseRepository;
        this.customerRepository = customerRepository;
        this.setRepository = setRepository;
        this.validateToken = validateToken;
    }

    @Transactional
    public void execute (WorkoutDto workoutDto, String authHeader){
        validateToken.execute(workoutDto.getCustomerId(), authHeader);
        initializeClassCustomerEntity(workoutDto.getCustomerId());
        editWorkout(workoutDto);
    }

    //criado pra 'economizar' passagens de referencia entre os metodos
    private void initializeClassCustomerEntity(Long id){
        Optional <CustomerEntity> customerEntityOptional = customerRepository.findById(id);
        if (customerEntityOptional.isPresent()){
            this.customerEntity = customerEntityOptional.get();
        }
        else {
            throw new CustomException.CustomerNotFound("Cliente não encontrado.");
        }
    }

    private void editWorkout(WorkoutDto workoutDto){
        WorkoutEntity workoutEntity;

        Optional<WorkoutEntity> workoutEntityOptional = workoutRepository.findById(workoutDto.getId());
        if (workoutEntityOptional.isPresent()){
            workoutEntity = workoutEntityOptional.get();
        }
        else {
            throw new CustomException.WorkoutNotFoundException("Treino não encontrado.");
        }

        //verificação de segurança
        if (!Objects.equals(this.customerEntity.getId(), workoutEntity.getCustomerEntity().getId())){
            throw new CustomException.CustomerTriedToAccessUnauthorizedWorkoutException("Esse cliente não possui permissão para este treino");
        }

        workoutEntity.setName(workoutDto.getName());

        //salvando
        workoutRepository.save(workoutEntity);

        //saindo da raiz da arvore
        editExercise(workoutDto.getExerciseDtoList(), workoutEntity);
    }

    private void editExercise(List<ExerciseDto> exerciseDtoList, WorkoutEntity workoutEntity){
        ExerciseEntity exerciseEntity;
        //iterando sobre a lista de exercicios
        for (int i = 0; i<exerciseDtoList.size(); i++) {
            //se for um novo exercicio, chama o serviço de adicionar novo exercicio
            if (exerciseDtoList.get(i).getId() == -1) {
                List<ExerciseDto> exerciseDtoListSingle = new ArrayList<>();
                exerciseDtoListSingle.add(exerciseDtoList.get(i));
                addWorkoutService.addExercise(exerciseDtoListSingle, workoutEntity);
            }
            //se não, busca o exercicio com id correspondente e o edita
            else {
                Optional<ExerciseEntity> exerciseEntityOptional = exerciseRepository.findById(exerciseDtoList.get(i).getId());
                if (exerciseEntityOptional.isPresent()) {
                    exerciseEntity = exerciseEntityOptional.get();
                } else {
                    throw new CustomException.ExerciseNotFoundException("Exercício não encontrado");
                }
                //verificação de segurança
                if (workoutEntity.getId() != exerciseEntity.getWorkoutEntity().getId()){
                    throw new CustomException.CustomerTriedToAccessUnauthorizedExerciseException("Esse cliente não possui permissão para este exercício");
                }
                exerciseEntity.setName(exerciseDtoList.get(i).getName());
                exerciseRepository.save(exerciseEntity);
                //indo para as folhas
                editSet(exerciseDtoList.get(i).getSetDtoList(), exerciseEntity);
            }
        }
    }

    private void editSet(List<SetDto> setDtoList, ExerciseEntity exerciseEntity){
        SetEntity setEntity;
        //iterando sobre a lista de sets
        for (int i = 0; i< setDtoList.size(); i++){
            //se for um novo set, chama o serviço de adicionar novo set
            if (setDtoList.get(i).getId() == -1){
                List <SetDto> setDtoListSingle = new ArrayList<>();
                setDtoListSingle.add(setDtoList.get(i));
                addWorkoutService.addSet(setDtoListSingle, exerciseEntity, this.customerEntity);
            }
            //se não, busca o set com id correspondente e o edita
            else {
                Optional<SetEntity> setEntityOptional = setRepository.findById(setDtoList.get(i).getId());
                if (setEntityOptional.isPresent()){
                    setEntity = setEntityOptional.get();
                }
                else {
                    throw new CustomException.SetNotFoundException("Série não encontrada");                }
                //verificacao de segurança
                if (exerciseEntity.getId() != setEntity.getExerciseEntity().getId()){
                    throw new CustomException.CustomerTriedToAccessUnauthorizedSetException("Esse cliente não possui permissão para esta série");
                }
                setEntity.setWeight(setDtoList.get(i).getWeight());
                setEntity.setRepetitions(setDtoList.get(i).getRepetitions());
                setRepository.save(setEntity);
            }
        }
    }
}
