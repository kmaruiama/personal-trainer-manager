package com.example.training_manager.Dto.Weight;

import lombok.Data;

@Data
public class WeightEditDto {
    private Long weightId;
    private float bodyFatPercentage;
    private float weight;
    private float height;
}
