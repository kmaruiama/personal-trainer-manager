package com.example.training_manager.Dto.Schedule;

import lombok.Data;

import java.time.LocalTime;
import java.util.Date;

@Data
public class ScheduleDto {
    private Long workoutId;
    private Long customerId;
    private int dayOfTheWeek;
    private LocalTime hourStart;
    private LocalTime hourEnd;
}
