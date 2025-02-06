package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.ProgramDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.ProgramEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.ProgramRepository;
import com.example.training_manager.Service.Shared.ValidateToken;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AddProgramService {
    private final ProgramRepository programRepository;
    private final ValidateToken validateToken;
    private final CustomerRepository customerRepository;

    @Autowired
    AddProgramService(ProgramRepository programRepository, ValidateToken validateToken, CustomerRepository customerRepository){
        this.programRepository = programRepository;
        this.validateToken = validateToken;
        this.customerRepository = customerRepository;

    }

    @Transactional
    public void execute (ProgramDto programDto, String authHeader){
        validateToken.execute(programDto.getCustomerId(), authHeader);
        ProgramEntity programEntity = new ProgramEntity();
        programEntity.setName(programDto.getName());
        CustomerEntity customerEntity;
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(programDto.getCustomerId());
        if(customerEntityOptional.isPresent()){
            customerEntity = customerEntityOptional.get();
        }
        else {
            throw new CustomException.CustomerNotFound("Cliente não encontrado.");
        }
        programEntity.setCustomerEntity(customerEntity);
        programEntity.setWorkouts(new ArrayList<>());
        programRepository.save(programEntity);
    }
}
