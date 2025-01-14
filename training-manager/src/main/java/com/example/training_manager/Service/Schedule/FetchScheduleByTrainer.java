package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Dto.Schedule.ScheduleGetDto;
import com.example.training_manager.Model.ScheduleEntity;
import com.example.training_manager.Model.TrainerEntity;
import com.example.training_manager.Repository.ScheduleRepository;
import com.example.training_manager.Repository.TrainerRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FetchScheduleByTrainer {
    private final ScheduleRepository scheduleRepository;
    private final TrainerRepository trainerRepository;

    @Autowired
    FetchScheduleByTrainer(ScheduleRepository scheduleRepository,
                           TrainerRepository trainerRepository) {
        this.scheduleRepository = scheduleRepository;
        this.trainerRepository = trainerRepository;
    }

    public List<ScheduleGetDto> execute(String authHeader) throws Exception {
        TrainerEntity trainerEntity = getTrainer(authHeader);
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findScheduleEntitiesByTrainer(trainerEntity);
        return transformAllEntitiesIntoDTOs(scheduleEntities);
    }

    private List<ScheduleGetDto> transformAllEntitiesIntoDTOs(List<ScheduleEntity> scheduleEntities) {
        List<ScheduleGetDto> list = new ArrayList<>();
        for (int i = 0; i < scheduleEntities.size(); i++) {
            ScheduleGetDto scheduleGetDto = new ScheduleGetDto();
            scheduleGetDto.setDayOfTheWeek(scheduleEntities.get(i).getDayOfTheWeek());
            scheduleGetDto.setHourStart(scheduleEntities.get(i).getHourStart());
            scheduleGetDto.setHourEnd(scheduleEntities.get(i).getHourEnd());
            scheduleGetDto.setCustomerName(scheduleEntities.get(i).getCustomerEntity().getNome());
            scheduleGetDto.setCustomerId(scheduleEntities.get(i).getCustomerEntity().getId());
            scheduleGetDto.setWorkoutName(scheduleEntities.get(i).getWorkoutEntity().getName());
            scheduleGetDto.setScheduleId(scheduleEntities.get(i).getId());
            list.add(scheduleGetDto);
        }
        return list;
    }

    //dps mergear esse metodo com outros iguais sla returnTrainerEntityService
    private TrainerEntity getTrainer(String authHeader) throws Exception{
        Optional<TrainerEntity> optionalTrainerEntity = trainerRepository
                .findById((ReturnTrainerIdFromJWT.execute(authHeader)));
        if (optionalTrainerEntity.isPresent()) {
            return optionalTrainerEntity.get();
        }
        return null;
    }
}
