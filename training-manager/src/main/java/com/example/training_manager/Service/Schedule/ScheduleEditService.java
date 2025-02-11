package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Dto.Schedule.ScheduleDto;
import com.example.training_manager.Dto.Schedule.ScheduleGetDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.ScheduleEntity;
import com.example.training_manager.Model.ScheduleMode;
import com.example.training_manager.Model.WorkoutEntity;
import com.example.training_manager.Repository.ScheduleRepository;
import com.example.training_manager.Repository.WorkoutRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateToken;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScheduleEditService {
    private final CheckCurrentSchedule checkCurrentSchedule;
    private final ScheduleRepository scheduleRepository;
    private final ValidateToken validateToken;
    private final WorkoutRepository workoutRepository;
    private final IntrospectScheduleService introspectScheduleService;

    ScheduleEditService(CheckCurrentSchedule checkCurrentSchedule,
                        ScheduleRepository scheduleRepository,
                        ValidateToken validateToken,
                        WorkoutRepository workoutRepository,
                        IntrospectScheduleService introspectScheduleService) {
        this.checkCurrentSchedule = checkCurrentSchedule;
        this.scheduleRepository = scheduleRepository;
        this.validateToken = validateToken;
        this.workoutRepository = workoutRepository;
        this.introspectScheduleService = introspectScheduleService;
    }

    @Transactional
    public void execute(String authHeader, ScheduleGetDto scheduleGetDto){
        validateToken.execute(scheduleGetDto.getCustomerId(), authHeader);

        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setHourStart(scheduleGetDto.getHourStart());
        scheduleDto.setHourEnd(scheduleGetDto.getHourEnd());
        scheduleDto.setDayOfTheWeek(scheduleGetDto.getDayOfTheWeek());

        if (!checkCurrentSchedule.execute(scheduleDto, authHeader)) {
            throw new CustomException.ScheduleConflictException("O horário conflita com outro existente.");
        }

        ScheduleEntity scheduleEntity;

        Optional<ScheduleEntity> scheduleEntityOptional = scheduleRepository.findById(scheduleGetDto.getScheduleId());
        if (scheduleEntityOptional.isPresent()) {
            scheduleEntity = scheduleEntityOptional.get();
            scheduleEntity.setDayOfTheWeek(scheduleGetDto.getDayOfTheWeek());
            scheduleEntity.setHourEnd(scheduleGetDto.getHourStart());
            scheduleEntity.setHourEnd(scheduleGetDto.getHourEnd());
            scheduleEntity.setWorkoutEntity(setWorkoutBlueprint(scheduleGetDto.getWorkoutId()));
        } else {
            throw new CustomException.ScheduleNotFoundException("Agendamento não encontrado");
        }

        if(scheduleEntity.getCustomerEntity().getScheduleMode() == ScheduleMode.BY_ORDER) {
            introspectScheduleService.execute(scheduleDto.getCustomerId(), authHeader);
        }
    }

    private WorkoutEntity setWorkoutBlueprint(Long workoutId){
        Optional <WorkoutEntity> workoutEntityOptional = workoutRepository.findById(workoutId);
        if (workoutEntityOptional.isPresent()){
            return workoutEntityOptional.get();
        }
        else throw new CustomException.WorkoutNotFoundException("Não foi possível encontrar o treino");
    }
}
