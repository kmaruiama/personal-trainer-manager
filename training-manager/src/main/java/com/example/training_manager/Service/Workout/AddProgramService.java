package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.ProgramDto;
import com.example.training_manager.Model.BlueprintEntity;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.ProgramEntity;
import com.example.training_manager.Repository.BlueprintRepository;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.ProgramRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddProgramService {
    private final AddWorkoutService addWorkoutService;
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final CustomerRepository customerRepository;
    private final ProgramRepository programRepository;
    private final BlueprintRepository blueprintRepository;

    AddProgramService(AddWorkoutService addWorkoutService,
                      ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer,
                      CustomerRepository customerRepository,
                      ProgramRepository programRepository,
                      BlueprintRepository blueprintRepository) {
        this.addWorkoutService = addWorkoutService;
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.customerRepository = customerRepository;
        this.programRepository = programRepository;
        this.blueprintRepository = blueprintRepository;
    }

    public void execute(ProgramDto programDto, String authHeader) throws Exception {
        if (!validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader), programDto.getCustomerId())) {
            throw new Exception("O treinador não possui permissão para este cliente.");
        }
        ProgramEntity programEntity = new ProgramEntity();
        setCustomer(programEntity, programDto.getCustomerId());
        programEntity.setName(programDto.getName());
        programRepository.save(programEntity);
        if(programDto.isBlueprint()){
            programEntity.setBlueprint(true);
            BlueprintEntity blueprintEntity = new BlueprintEntity();
            blueprintEntity.setProgramEntity(programEntity);
            setCustomerBlueprint(blueprintEntity, programDto.getCustomerId());
            blueprintRepository.save(blueprintEntity);
        }
        for (int i = 0; i < programDto.getWorkouts().size(); i++) {
            addWorkoutService.execute(programDto.getWorkouts().get(i), programEntity);
        }
    }

    private void setCustomer(ProgramEntity programEntity, Long customerId) {
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(customerId);
        if (customerEntityOptional.isPresent()) {
            programEntity.setCustomerEntity(customerEntityOptional.get());
        }
    }

    private void setCustomerBlueprint(BlueprintEntity blueprintEntity, Long customerId){
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(customerId);
        if (customerEntityOptional.isPresent()) {
            blueprintEntity.setCustomerEntity(customerEntityOptional.get());
        }
    }
}