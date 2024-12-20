package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.WorkoutDto;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.ProgramEntity;
import com.example.training_manager.Model.WorkoutEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddWorkoutService {
    private final AddExerciseService addExerciseService;
    private final CustomerRepository customerRepository;
    private final WorkoutRepository workoutRepository;

    @Autowired
    public AddWorkoutService(AddExerciseService addExerciseService,
                             CustomerRepository customerRepository,
                             WorkoutRepository workoutRepository) {
        this.addExerciseService = addExerciseService;
        this.customerRepository = customerRepository;
        this.workoutRepository = workoutRepository;
    }

    public void execute(WorkoutDto workoutDto, ProgramEntity programEntity) {
        WorkoutEntity workoutEntity = new WorkoutEntity();

        workoutEntity.setProgramEntity(programEntity);
        workoutEntity.setName(workoutDto.getName());
        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(workoutDto.getCustomerId());
        if (optionalCustomerEntity.isPresent()) {
            CustomerEntity customerEntity = optionalCustomerEntity.get();
            workoutEntity.setCustomerEntity(customerEntity);
        }
        workoutRepository.save(workoutEntity);
        int index = 0;
        while (index < workoutDto.getExercises().size()) {
            addExerciseService.execute(workoutDto.getExercises().get(index), workoutEntity);
            index++;
        }
    }
}