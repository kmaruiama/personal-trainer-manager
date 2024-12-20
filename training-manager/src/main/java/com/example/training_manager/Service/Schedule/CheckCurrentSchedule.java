package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Dto.Schedule.ScheduleDto;
import com.example.training_manager.Model.ScheduleEntity;
import com.example.training_manager.Model.TrainerEntity;
import com.example.training_manager.Repository.ScheduleRepository;
import com.example.training_manager.Repository.TrainerRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
//excluir esse service e jogar pro frontend depois
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

    public boolean execute(ScheduleDto scheduleDto, String authHeader) throws Exception {
        List<ScheduleEntity> allSchedules = scheduleRepository.findScheduleEntitiesByTrainer(getTrainer(authHeader));
        List<LocalDateTime[]> scheduleLocalDateTime = transformAllSchedulesIntoLocalDateTimeArrays(allSchedules);
        LocalDateTime[] wantsToGetIn = dateToLocalDateTime(scheduleDto.getDateStart(), scheduleDto.getDateEnd());
        for (int i = 0; i < scheduleLocalDateTime.size(); i++) {
            LocalDateTime[] current = scheduleLocalDateTime.get(i);
            if (schedulesOverlap(current, wantsToGetIn)){
                return false;
            }
        }
        return true;
    }

    private boolean schedulesOverlap(LocalDateTime[] alreadySaved, LocalDateTime[] wantsToGetIn) {
        DayOfWeek alreadySavedDay = alreadySaved[0].getDayOfWeek();
        DayOfWeek wantsToGetInDay = wantsToGetIn[0].getDayOfWeek();

        if (!alreadySavedDay.equals(wantsToGetInDay)) {
            return false;
        }

        LocalTime alreadySavedStart = alreadySaved[0].toLocalTime();
        LocalTime alreadySavedEnd = alreadySaved[1].toLocalTime();
        LocalTime wantsToGetInStart = wantsToGetIn[0].toLocalTime();
        LocalTime wantsToGetInEnd = wantsToGetIn[1].toLocalTime();

        return !alreadySavedEnd.isBefore(wantsToGetInStart) && !alreadySavedStart.isAfter(wantsToGetInEnd);
    }

    private TrainerEntity getTrainer(String authHeader) throws Exception{
        Optional<TrainerEntity> optionalTrainerEntity = trainerRepository
                .findById(
                        ReturnTrainerIdFromJWT.execute(authHeader));
        if (optionalTrainerEntity.isPresent()) {
            return optionalTrainerEntity.get();
        }
        return null;
    }

    private List<LocalDateTime[]> transformAllSchedulesIntoLocalDateTimeArrays(List<ScheduleEntity> allSchedules) {
        List<LocalDateTime[]> list = new ArrayList<>();
        for (int i = 0; i < allSchedules.size(); i++) {
            LocalDateTime startTime = allSchedules.get(i)
                    .getDateStart()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            LocalDateTime endTime = allSchedules.get(i)
                    .getDateEnd()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            list.add(new LocalDateTime[]{startTime, endTime});
        }
        return list;
    }

    private LocalDateTime[] dateToLocalDateTime(Date dateStart, Date dateEnd){
        LocalDateTime startTime = dateStart
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime endTime = dateEnd
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return new LocalDateTime[]{startTime, endTime};
    }
}
