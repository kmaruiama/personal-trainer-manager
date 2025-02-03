package com.example.training_manager.Service.BodyComposition;

import com.example.training_manager.Dto.Weight.WeightDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.WeightEntity;
import com.example.training_manager.Repository.WeightRepository;
import com.example.training_manager.Service.Shared.ValidateToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetrieveLastInput {
    private final WeightRepository weightRepository;
    private final ValidateToken validateToken;

    @Autowired
    RetrieveLastInput(WeightRepository weightRepository, ValidateToken validateToken){
        this.weightRepository = weightRepository;
        this.validateToken = validateToken;
    }

    public WeightDto execute (Long id, String authHeader){
        validateToken.execute(id, authHeader);
        WeightEntity weightEntity = weightRepository.findTopByCustomerEntityIdOrderByDateDesc(id);
        if(weightEntity == null){
            throw new CustomException.CannotRetrieveLastCustomerWeightInputException("Erro ao retornar último input de peso.");
        }
        WeightDto weightDto = new WeightDto();
        weightDto.setWeight(weightEntity.getWeight());
        weightDto.setHeight(weightEntity.getHeight());
        weightDto.setBodyFatPercentage(weightEntity.getBodyFatPercentage());
        return weightDto;
    }
}
