package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Repository.ScheduleRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateToken;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleDeleteService {
    private final ScheduleRepository scheduleRepository;
    private final ValidateToken validateToken;

    @Autowired
    ScheduleDeleteService(ScheduleRepository scheduleRepository, ValidateToken validateToken) {
        this.scheduleRepository = scheduleRepository;
        this.validateToken = validateToken;
    }

    @Transactional
    public void execute(String authHeader, Long id){
        validateToken.execute(scheduleRepository.findCustomerIdByScheduleId(id), authHeader);
        scheduleRepository.deleteById(id);
    }
}
