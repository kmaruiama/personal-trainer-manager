package com.example.training_manager.Service.BodyComposition;

import com.example.training_manager.Repository.WeightRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateToken;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeightDeleteService {
    private final WeightRepository weightRepository;
    private final ValidateToken validateToken;

    @Autowired
    WeightDeleteService(WeightRepository weightRepository,
                        ValidateToken validateToken){
        this.weightRepository = weightRepository;
        this.validateToken = validateToken;
    }

    @Transactional
    public void execute(Long id, String authHeader){
        Long customerId = weightRepository.findCustomerIdByWeightId(id);
        validateToken.execute(customerId, authHeader);
        weightRepository.deleteById(id);
    }
}
