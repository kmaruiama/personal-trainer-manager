package com.example.training_manager.Dto.Schedule;

import lombok.Data;

import java.util.Date;

@Data
public class ScheduleDto {
    private Long workoutId;
    private Long customerId;
    private Date dateStart;
    private Date dateEnd;
}
