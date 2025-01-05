package com.example.training_manager.Service.Shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateToken {
    ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    @Autowired
    ValidateToken(ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer){
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
    }
    public void execute (Long customerId, String authHeader) throws Exception{
        if (!validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader), customerId)) {
            throw new Exception("O treinador não possui permissão para este cliente.");
        }
    }
}
