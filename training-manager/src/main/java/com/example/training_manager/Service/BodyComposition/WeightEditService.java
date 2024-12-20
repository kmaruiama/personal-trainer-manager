package com.example.training_manager.Service.BodyComposition;

import com.example.training_manager.Dto.Weight.WeightEditDto;
import com.example.training_manager.Model.WeightEntity;
import com.example.training_manager.Repository.WeightRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WeightEditService {
    WeightRepository weightRepository;
    ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;

    @Autowired
    WeightEditService(WeightRepository weightRepository){
        this.weightRepository = weightRepository;
    }
    public void execute(WeightEditDto weightEditDto, String authHeader) throws Exception{
        WeightEntity weightEntity = null;
        Optional<WeightEntity> weightEntityOptional = weightRepository.findById(weightEditDto.getWeightId());
        if (weightEntityOptional.isPresent()){
            weightEntity = weightEntityOptional.get();
        }
        if (weightEntity == null){
            throw new Exception("Não foi possível encontrar o registro");
        }
        if (validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader),weightEntity.getCustomerEntity().getId())){
            throw new Exception("O treinador não possui permissão para este cliente.");
        }
        weightEntity.setWeight(weightEditDto.getWeight());
        weightEntity.setBodyFatPercentage(weightEditDto.getBodyFatPercentage());
        weightRepository.save(weightEntity);
    }
}
