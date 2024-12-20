package com.example.training_manager.Dto.Schedule;

import lombok.Data;

import java.util.Date;

@Data
public class ScheduleGetDto {
    private String workoutName;
    private String customerName;
    private Date dateStart;
    private Date dateEnd;
    private Long scheduleId;
    private Long customerId;
}