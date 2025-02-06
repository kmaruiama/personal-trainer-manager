package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Dto.Schedule.ScheduleDto;
import com.example.training_manager.Dto.Schedule.ScheduleGetDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.ScheduleEntity;
import com.example.training_manager.Repository.ScheduleRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateToken;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScheduleEditService {
    private final CheckCurrentSchedule checkCurrentSchedule;
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final ScheduleRepository scheduleRepository;
    private final ValidateToken validateToken;

    ScheduleEditService(CheckCurrentSchedule checkCurrentSchedule,
                        ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer,
                        ScheduleRepository scheduleRepository, ValidateToken validateToken) {
        this.checkCurrentSchedule = checkCurrentSchedule;
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.scheduleRepository = scheduleRepository;
        this.validateToken = validateToken;
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

        Optional<ScheduleEntity> scheduleEntityOptional = scheduleRepository.findById(scheduleGetDto.getScheduleId());
        if (scheduleEntityOptional.isPresent()) {
            ScheduleEntity scheduleEntity = scheduleEntityOptional.get();
            scheduleEntity.setDayOfTheWeek(scheduleGetDto.getDayOfTheWeek());
            scheduleEntity.setHourEnd(scheduleGetDto.getHourStart());
            scheduleEntity.setHourEnd(scheduleGetDto.getHourEnd());
            //botar o treino tb dps de testar o refactoring
        } else {
            throw new CustomException.ScheduleNotFoundException("Agendamento não encontrado");
        }
    }
}
