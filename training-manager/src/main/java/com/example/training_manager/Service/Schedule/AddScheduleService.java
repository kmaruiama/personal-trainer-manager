package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Dto.Schedule.ScheduleDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.ScheduleEntity;
import com.example.training_manager.Model.TrainerEntity;
import com.example.training_manager.Model.WorkoutEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.ScheduleRepository;
import com.example.training_manager.Repository.TrainerRepository;
import com.example.training_manager.Repository.WorkoutRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateToken;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddScheduleService {
    private final CustomerRepository customerRepository;
    private final TrainerRepository trainerRepository;
    private final ScheduleRepository scheduleRepository;
    private final WorkoutRepository workoutRepository;
    private final CheckCurrentSchedule checkCurrentSchedule;
    private final ValidateToken validateToken;
    private final IntrospectScheduleService introspectScheduleService;

    @Autowired
    public AddScheduleService(CustomerRepository customerRepository,
                              TrainerRepository trainerRepository,
                              ScheduleRepository scheduleRepository,
                              WorkoutRepository workoutRepository,
                              CheckCurrentSchedule checkCurrentSchedule, ValidateToken validateToken, IntrospectScheduleService introspectScheduleService) {
        this.customerRepository = customerRepository;
        this.trainerRepository = trainerRepository;
        this.scheduleRepository = scheduleRepository;
        this.workoutRepository = workoutRepository;
        this.checkCurrentSchedule = checkCurrentSchedule;
        this.validateToken = validateToken;
        this.introspectScheduleService = introspectScheduleService;
    }

    @Transactional
    public void execute(String authHeader, ScheduleDto scheduleDto){
        validateToken.execute(scheduleDto.getCustomerId(), authHeader);

        if (!checkCurrentSchedule.execute(scheduleDto, authHeader)) {
            throw new CustomException.ScheduleConflictException("Esse horário já foi ocupado");
        }

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        setCustomer(scheduleEntity, scheduleDto);
        setWorkoutBlueprint(scheduleEntity, scheduleDto);
        setTrainer(scheduleEntity, authHeader);
        scheduleEntity.setDayOfTheWeek(scheduleDto.getDayOfTheWeek());
        scheduleEntity.setHourStart(scheduleDto.getHourStart());
        scheduleEntity.setHourEnd(scheduleDto.getHourEnd());
        scheduleEntity.setDone(false);
        scheduleRepository.save(scheduleEntity);
        introspectScheduleService.execute(scheduleDto.getCustomerId(), authHeader);
    }

    private void setCustomer(ScheduleEntity scheduleEntity, ScheduleDto scheduleDto) {
        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(scheduleDto.getCustomerId());
        if (optionalCustomerEntity.isPresent()) {
            CustomerEntity customerEntity = optionalCustomerEntity.get();
            scheduleEntity.setCustomerEntity(customerEntity);
        }
        else throw new CustomException.CustomerNotFound("Cliente não encontrado");
    }

    private void setTrainer(ScheduleEntity scheduleEntity, String authHeader) {
        Optional<TrainerEntity> optionalTrainerEntity = trainerRepository.findById(ReturnTrainerIdFromJWT.execute(authHeader));
        if (optionalTrainerEntity.isPresent()) {
            TrainerEntity trainerEntity = optionalTrainerEntity.get();
            scheduleEntity.setTrainerEntity(trainerEntity);
        }
        else throw new CustomException.TrainerNotFound("Treinador não encontrado");
    }

    private void setWorkoutBlueprint(ScheduleEntity scheduleEntity, ScheduleDto scheduleDto) {
        Optional<WorkoutEntity> optionalWorkoutBlueprintEntity = workoutRepository.findById(scheduleDto.getWorkoutId());
        if (optionalWorkoutBlueprintEntity.isPresent()) {
            WorkoutEntity workoutEntity = optionalWorkoutBlueprintEntity.get();
            scheduleEntity.setWorkoutEntity(workoutEntity);
        }
        else throw new CustomException.WorkoutNotFoundException("Treino não encontrado");
    }
}
