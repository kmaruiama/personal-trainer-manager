package com.example.training_manager.Service.BodyComposition;

import com.example.training_manager.Dto.Weight.WeightDto;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.WeightEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.WeightRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class AddWeightService {
    ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    WeightRepository weightRepository;
    CustomerRepository customerRepository;

    @Autowired
    AddWeightService(ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer,
                     WeightRepository weightRepository,
                     CustomerRepository customerRepository) {
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.weightRepository = weightRepository;
        this.customerRepository = customerRepository;
    }

    public void execute(WeightDto weightDto, String authHeader) throws Exception {
        if (!validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader), weightDto.getCustomerId())) {
            throw new Exception("O treinador não possui permissão para este cliente");
        }
        WeightEntity weightEntity = new WeightEntity();
        weightEntity.setBodyFatPercentage(weightDto.getBodyFatPercentage());
        weightEntity.setWeight(weightDto.getWeight());
        weightEntity.setHeight(weightDto.getHeight());
        setCustomer(weightEntity, weightDto);
        if (weightDto.getDate() != null) {
            weightEntity.setDate(weightDto.getDate());
        } else {
            Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
            weightEntity.setDate(today);
        }
        weightRepository.save(weightEntity);
    }

    private void setCustomer(WeightEntity weightEntity, WeightDto weightDto) {
        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(weightDto.getCustomerId());
        if (optionalCustomerEntity.isPresent()) {
            CustomerEntity customerEntity = optionalCustomerEntity.get();
            weightEntity.setCustomerEntity(customerEntity);
        }
    }
}
