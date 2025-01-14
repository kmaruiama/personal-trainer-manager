package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Dto.Schedule.ScheduleDto;
import com.example.training_manager.Dto.Schedule.ScheduleGetDto;
import com.example.training_manager.Model.ScheduleEntity;
import com.example.training_manager.Repository.ScheduleRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScheduleEditService {
    private final CheckCurrentSchedule checkCurrentSchedule;
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final ScheduleRepository scheduleRepository;

    ScheduleEditService(CheckCurrentSchedule checkCurrentSchedule,
                        ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer,
                        ScheduleRepository scheduleRepository) {
        this.checkCurrentSchedule = checkCurrentSchedule;
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.scheduleRepository = scheduleRepository;
    }

    public void execute(String authHeader, ScheduleGetDto scheduleGetDto) throws Exception {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setHourStart(scheduleGetDto.getHourStart());
        scheduleDto.setHourEnd(scheduleGetDto.getHourEnd());
        scheduleDto.setDayOfTheWeek(scheduleGetDto.getDayOfTheWeek());

        if (!checkCurrentSchedule.execute(scheduleDto, authHeader)) {
            throw new Exception("O horário conflita com outro existente.");
        }

        if (!validateTrainerOwnershipOverCustomer.execute(
                ReturnTrainerIdFromJWT.execute(authHeader), scheduleGetDto.getCustomerId())) {
            throw new Exception("O treinador não possui permissão para este cliente.");
        }

        Optional<ScheduleEntity> scheduleEntityOptional = scheduleRepository.findById(scheduleGetDto.getScheduleId());
        if (scheduleEntityOptional.isPresent()) {
            ScheduleEntity scheduleEntity = scheduleEntityOptional.get();
            scheduleEntity.setDayOfTheWeek(scheduleGetDto.getDayOfTheWeek());
            scheduleEntity.setHourEnd(scheduleGetDto.getHourStart());
            scheduleEntity.setHourEnd(scheduleGetDto.getHourEnd());
            //botar o treino tb dps de testar o refactoring
            scheduleRepository.save(scheduleEntity);
        } else {
            throw new Exception("Erro inesperado");
        }
    }
}
