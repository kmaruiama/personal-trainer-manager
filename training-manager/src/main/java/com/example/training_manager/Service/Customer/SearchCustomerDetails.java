package com.example.training_manager.Service.Customer;


import com.example.training_manager.Dto.Customer.CustomerInfoDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateToken;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class SearchCustomerDetails {
    private final ValidateToken validateToken;
    private final CustomerRepository customerRepository;

    @Autowired
    public SearchCustomerDetails(ValidateToken validateToken,
                                 CustomerRepository customerRepository) {
        this.validateToken = validateToken;
        this.customerRepository = customerRepository;
    }

    public CustomerInfoDto execute(String authHeader, Long id){
        validateToken.execute(id, authHeader);
        CustomerEntity customerEntity;
        CustomerInfoDto customerInfoDto = new CustomerInfoDto();
        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(id);
        if (optionalCustomerEntity.isPresent())
        {
            customerEntity = optionalCustomerEntity.get();
        }
        else
        {
            throw new CustomException.CustomerNotFound("Cliente com esse id não encontrado.");
        }
        customerInfoDto.setId(customerEntity.getId());
        customerInfoDto.setName(customerEntity.getNome());
        customerInfoDto.setBirth(customerEntity.getDataNascimento());
        customerInfoDto.setCpf(customerEntity.getCpf());
        customerInfoDto.setAddress(customerEntity.getEndereco());
        return customerInfoDto;
    }
}
