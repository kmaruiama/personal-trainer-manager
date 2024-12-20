package com.example.training_manager.Service.BodyComposition;

import com.example.training_manager.Dto.Weight.WeightDto;
import com.example.training_manager.Model.WeightEntity;
import com.example.training_manager.Repository.WeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetrieveLastInput {
    private final WeightRepository weightRepository;

    @Autowired
    RetrieveLastInput(WeightRepository weightRepository){
        this.weightRepository = weightRepository;
    }
    public WeightDto execute (Long id, String authHeader) throws Exception{
        WeightEntity weightEntity = weightRepository.findTopByCustomerEntityIdOrderByDateDesc(id);
        if(weightEntity == null){
            throw new Exception("erro ao retornar último input de peso");
        }
        WeightDto weightDto = new WeightDto();
        weightDto.setWeight(weightEntity.getWeight());
        weightDto.setHeight(weightEntity.getHeight());
        weightDto.setBodyFatPercentage(weightEntity.getBodyFatPercentage());
        return weightDto;
    }
}
