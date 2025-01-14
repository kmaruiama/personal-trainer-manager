package com.example.training_manager.Dto.Schedule;

import lombok.Data;

import java.time.LocalTime;
import java.util.Date;

@Data
public class ScheduleGetDto {
    private String workoutName;
    private String customerName;
    private int dayOfTheWeek;
    private LocalTime hourStart;
    private LocalTime hourEnd;
    private Long scheduleId;
    private Long customerId;
}