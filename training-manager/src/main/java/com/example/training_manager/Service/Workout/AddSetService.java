package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.SetDto;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.ExerciseEntity;
import com.example.training_manager.Model.SetEntity;
import com.example.training_manager.Repository.SetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddSetService {
    private final SetRepository setRepository;

    @Autowired
    AddSetService(SetRepository setRepository){
        this.setRepository = setRepository;
    }

    public void execute(List<SetDto> setDtoList, ExerciseEntity exerciseEntity, CustomerEntity customerEntity){
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
