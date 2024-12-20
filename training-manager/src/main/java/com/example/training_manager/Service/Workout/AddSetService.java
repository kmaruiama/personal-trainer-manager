package com.example.training_manager.Service.Workout;


import com.example.training_manager.Dto.Workout.SetDto;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.ExerciseEntity;
import com.example.training_manager.Model.SetEntity;
import com.example.training_manager.Repository.SetRepository;
import org.springframework.stereotype.Service;

@Service
public class AddSetService {

    private final SetRepository setRepository;

    AddSetService(SetRepository setRepository){
        this.setRepository = setRepository;
    }

    public void execute(ExerciseEntity exerciseEntity, SetDto setDto, CustomerEntity customerEntity){
        SetEntity setEntity = new SetEntity();
        setEntity.setRepetitions(setDto.getRepetitions());
        setEntity.setWeight(setDto.getWeight());
        setEntity.setCustomerEntity(customerEntity);
        setEntity.setExerciseEntity(exerciseEntity);
        setRepository.save(setEntity);
    }
}
