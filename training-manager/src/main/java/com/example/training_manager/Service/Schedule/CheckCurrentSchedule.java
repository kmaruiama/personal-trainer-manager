package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Dto.Schedule.ScheduleDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.ScheduleEntity;
import com.example.training_manager.Model.TrainerEntity;
import com.example.training_manager.Repository.ScheduleRepository;
import com.example.training_manager.Repository.TrainerRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Service
public class CheckCurrentSchedule {
    private final ScheduleRepository scheduleRepository;
    private final TrainerRepository trainerRepository;

    @Autowired
    CheckCurrentSchedule(ScheduleRepository scheduleRepository,
                         TrainerRepository trainerRepository) {
        this.scheduleRepository = scheduleRepository;
        this.trainerRepository = trainerRepository;
    }

    public boolean execute(ScheduleDto scheduleDto, String authHeader){
        List<ScheduleEntity> allSchedules = scheduleRepository.findScheduleEntitiesByTrainer(getTrainer(authHeader));
        return checkSchedule(scheduleDto, allSchedules);
    }

    private boolean checkSchedule(ScheduleDto scheduleDto, List<ScheduleEntity> allSchedules) {
        for (ScheduleEntity existingSchedule : allSchedules) {
            if (scheduleDto.getDayOfTheWeek() == existingSchedule.getDayOfTheWeek()) {
                LocalTime newStart = scheduleDto.getHourStart();
                LocalTime newEnd = scheduleDto.getHourEnd();
                LocalTime existingStart = existingSchedule.getHourStart();
                LocalTime existingEnd = existingSchedule.getHourEnd();
                if (!(newEnd.isBefore(existingStart) || newStart.isAfter(existingEnd))) {
                    return false;
                }
            }
        }
        return true;
    }

    private TrainerEntity getTrainer(String authHeader){
        Optional<TrainerEntity> optionalTrainerEntity = trainerRepository
                .findById(
                        ReturnTrainerIdFromJWT.execute(authHeader));
        if (optionalTrainerEntity.isPresent()) {
            return optionalTrainerEntity.get();
        }
        else throw new CustomException.TrainerNotFound("Treinador não encontrado.");
    }
}
