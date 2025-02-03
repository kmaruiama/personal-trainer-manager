package com.example.training_manager.Service.BodyComposition;

import com.example.training_manager.Dto.Weight.WeightDto;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.WeightEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.WeightRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateToken;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class AddWeightService {

    private final WeightRepository weightRepository;
    private final CustomerRepository customerRepository;
    private final ValidateToken validateToken;

    @Autowired
    AddWeightService(WeightRepository weightRepository,
                     CustomerRepository customerRepository,
                     ValidateToken validateToken) {
        this.validateToken = validateToken;
        this.weightRepository = weightRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public void execute(WeightDto weightDto, String authHeader) {
        validateToken.execute(weightDto.getCustomerId(), authHeader);
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
        //aparentemente o transactional apenas atualiza entidades que já estão persisitidas no banco de dados
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
