package com.example.training_manager.Service.BodyComposition;

import com.example.training_manager.Dto.Weight.WeightEditDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.WeightEntity;
import com.example.training_manager.Repository.WeightRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateToken;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WeightEditService {
    private final ValidateToken validateToken;
    WeightRepository weightRepository;
    ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;

    @Autowired
    WeightEditService(WeightRepository weightRepository,
                      ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer, ValidateToken validateToken){
        this.weightRepository = weightRepository;
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.validateToken = validateToken;
    }

    //a versão anterior dessa classe foi um erro que não pode mais se repetir X_X
    @Transactional
    public void execute(WeightEditDto weightEditDto, String authHeader){
        Long customerId = weightRepository.findCustomerIdByWeightId(weightEditDto.getWeightId());
        validateToken.execute(customerId, authHeader);

        WeightEntity weightEntity = null;
        Optional<WeightEntity> weightEntityOptional = weightRepository.findById(weightEditDto.getWeightId());
        if (weightEntityOptional.isPresent()){
            weightEntity = weightEntityOptional.get();
        }
        else{
            throw new CustomException.CannotRetrieveWeightException("Não foi possível encontrar o registro");
        }
        weightEntity.setWeight(weightEditDto.getWeight());
        weightEntity.setBodyFatPercentage(weightEditDto.getBodyFatPercentage());
    }
}
