package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Repository.ScheduleRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleDeleteService {
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    ScheduleDeleteService(ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer,
                          ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
    }

    public void execute(String authHeader, Long id) throws Exception {
        if (!validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader), scheduleRepository.findCustomerIdByScheduleId(id))) {
            throw new Exception("O treinador não possui permissão para este cliente.");
        }
        scheduleRepository.deleteById(id);
    }
}
