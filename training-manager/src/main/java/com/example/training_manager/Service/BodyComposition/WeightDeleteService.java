package com.example.training_manager.Service.BodyComposition;

import com.example.training_manager.Repository.WeightRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeightDeleteService {
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final WeightRepository weightRepository;

    @Autowired
    WeightDeleteService(WeightRepository weightRepository,
                        ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer){
        this.weightRepository = weightRepository;
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
    }

    public void execute(Long id, String authHeader) throws Exception{
        Long customerId = weightRepository.findCustomerIdByWeightId(id);
        if (validateTrainerOwnershipOverCustomer.execute(customerId, ReturnTrainerIdFromJWT.execute(authHeader))){
            throw new Exception("O treinador não possui permissão para este cliente.");
        }
        weightRepository.deleteById(id);
    }
}
