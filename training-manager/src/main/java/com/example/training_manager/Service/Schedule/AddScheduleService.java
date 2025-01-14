package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Dto.Schedule.ScheduleDto;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.ScheduleEntity;
import com.example.training_manager.Model.TrainerEntity;
import com.example.training_manager.Model.WorkoutEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.ScheduleRepository;
import com.example.training_manager.Repository.TrainerRepository;
import com.example.training_manager.Repository.WorkoutRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddScheduleService {
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final CustomerRepository customerRepository;
    private final TrainerRepository trainerRepository;
    private final ScheduleRepository scheduleRepository;
    private final WorkoutRepository workoutRepository;
    private final CheckCurrentSchedule checkCurrentSchedule;

    @Autowired
    public AddScheduleService(ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer,
                              CustomerRepository customerRepository,
                              TrainerRepository trainerRepository,
                              ScheduleRepository scheduleRepository,
                              WorkoutRepository workoutRepository,
                              CheckCurrentSchedule checkCurrentSchedule) {
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.customerRepository = customerRepository;
        this.trainerRepository = trainerRepository;
        this.scheduleRepository = scheduleRepository;
        this.workoutRepository = workoutRepository;
        this.checkCurrentSchedule = checkCurrentSchedule;
    }

    public void execute(String authHeader, ScheduleDto scheduleDto) throws Exception {
        if (!checkCurrentSchedule.execute(scheduleDto, authHeader)) {
            throw new Exception("O horário conflita com outro existente.");
        }

        if (!validateTrainerOwnershipOverCustomer.execute(
                ReturnTrainerIdFromJWT.execute(authHeader), scheduleDto.getCustomerId())) {
            throw new Exception("O treinador não possui permissão para este cliente.");
        }

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        setCustomer(scheduleEntity, scheduleDto);
        setWorkoutBlueprint(scheduleEntity, scheduleDto);
        setTrainer(scheduleEntity, authHeader);
        scheduleEntity.setDayOfTheWeek(scheduleDto.getDayOfTheWeek());
        scheduleEntity.setHourStart(scheduleDto.getHourStart());
        scheduleEntity.setHourEnd(scheduleDto.getHourEnd());
        scheduleRepository.save(scheduleEntity);
    }

    private void setCustomer(ScheduleEntity scheduleEntity, ScheduleDto scheduleDto) {
        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(scheduleDto.getCustomerId());
        if (optionalCustomerEntity.isPresent()) {
            CustomerEntity customerEntity = optionalCustomerEntity.get();
            scheduleEntity.setCustomerEntity(customerEntity);
        }
    }

    private void setTrainer(ScheduleEntity scheduleEntity, String authHeader) throws Exception {
        Optional<TrainerEntity> optionalTrainerEntity = trainerRepository.findById(ReturnTrainerIdFromJWT.execute(authHeader));
        if (optionalTrainerEntity.isPresent()) {
            TrainerEntity trainerEntity = optionalTrainerEntity.get();
            scheduleEntity.setTrainerEntity(trainerEntity);
        }
    }

    private void setWorkoutBlueprint(ScheduleEntity scheduleEntity, ScheduleDto scheduleDto) {
        Optional<WorkoutEntity> optionalWorkoutBlueprintEntity = workoutRepository.findById(scheduleDto.getWorkoutId());
        if (optionalWorkoutBlueprintEntity.isPresent()) {
            WorkoutEntity workoutEntity = optionalWorkoutBlueprintEntity.get();
            scheduleEntity.setWorkoutEntity(workoutEntity);
        }
    }
}
