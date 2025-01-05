package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.ExerciseDto;
import com.example.training_manager.Dto.Workout.SetDto;
import com.example.training_manager.Dto.Workout.WorkoutDto;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.ExerciseEntity;
import com.example.training_manager.Model.SetEntity;
import com.example.training_manager.Model.WorkoutEntity;
import com.example.training_manager.Repository.CustomerRepository;
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
public class EditWorkoutService {
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final WorkoutRepository workoutRepository;
    private final AddWorkoutService addWorkoutService;
    private final ExerciseRepository exerciseRepository;
    private final CustomerRepository customerRepository;
    private final SetRepository setRepository;
    private CustomerEntity customerEntity;

    @Autowired
    EditWorkoutService(ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer,
                       WorkoutRepository workoutRepository,
                       AddWorkoutService addWorkoutService, ExerciseRepository exerciseRepository, CustomerRepository customerRepository, SetRepository setRepository){
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.workoutRepository = workoutRepository;
        this.addWorkoutService = addWorkoutService;
        this.exerciseRepository = exerciseRepository;
        this.customerRepository = customerRepository;
        this.setRepository = setRepository;
    }

    public void execute (WorkoutDto workoutDto, String authHeader) throws Exception{
        //validacao de segurança
        if (!validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader), workoutDto.getCustomerId())) {
            throw new Exception("O treinador não possui permissão para este cliente.");
        }
        initializeClassCustomerEntity(workoutDto.getCustomerId());
        editWorkout(workoutDto);
    }

    //criado pra 'economizar' passagens de referencia entre os metodos
    private void initializeClassCustomerEntity(Long id) throws Exception{
        Optional <CustomerEntity> customerEntityOptional = customerRepository.findById(id);
        if (customerEntityOptional.isPresent()){
            this.customerEntity = customerEntityOptional.get();
        }
        else {
            throw new Exception("Erro ao encontrar cliente");
        }
    };

    private void editWorkout(WorkoutDto workoutDto) throws Exception{
        WorkoutEntity workoutEntity;

        Optional<WorkoutEntity> workoutEntityOptional = workoutRepository.findById(workoutDto.getId());
        if (workoutEntityOptional.isPresent()){
            workoutEntity = workoutEntityOptional.get();
        }
        else {
            throw new Exception("Erro ao encontrar treino");
        }
        workoutEntity.setName(workoutDto.getName());

        //salvando
        workoutRepository.save(workoutEntity);

        //saindo da raiz da arvore
        editExercise(workoutDto.getExercises(), workoutEntity);
    }

    private void editExercise(List<ExerciseDto> exerciseDtoList, WorkoutEntity workoutEntity) throws Exception{
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
                    throw new Exception("Erro ao encontrar exercício");
                }
                exerciseEntity.setName(exerciseDtoList.get(i).getName());
                exerciseRepository.save(exerciseEntity);
                //indo para as folhas
                editSet(exerciseDtoList.get(i).getSetDtoList(), exerciseEntity);
            }
        }
    }

    private void editSet(List<SetDto> setDtoList, ExerciseEntity exerciseEntity) throws Exception{
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
                //se não achar, joga exception
                else {
                    throw new Exception("Erro ao editar série");
                }
                setEntity.setWeight(setDtoList.get(i).getWeight());
                setEntity.setRepetitions(setDtoList.get(i).getRepetitions());
                setRepository.save(setEntity);
            }
        }
    }
}
