package com.example.training_manager.Dto.Weight;

import lombok.Data;

import java.util.Date;

@Data
public class WeightDto {
    private Long customerId;
    private Date date;
    private float bodyFatPercentage;
    private float weight;
    private float height;
}
