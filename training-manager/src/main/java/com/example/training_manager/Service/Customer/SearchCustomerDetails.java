package com.example.training_manager.Service.Customer;


import com.example.training_manager.Dto.Customer.CustomerInfoDto;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SearchCustomerDetails {
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final CustomerRepository customerRepository;

    @Autowired
    public SearchCustomerDetails(ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer,
                                 CustomerRepository customerRepository) {
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.customerRepository = customerRepository;
    }

    public CustomerInfoDto execute(String authHeader, Long id) throws Exception{
        CustomerEntity customerEntity;
        if(!validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader), id))
        {
            throw new Exception("Esse treinador não possui permissão para este cliente");
        }
        CustomerInfoDto customerInfoDto = new CustomerInfoDto();
        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(id);
        if (optionalCustomerEntity.isPresent()){
            customerEntity = optionalCustomerEntity.get();
        }
        else{
            throw new Exception("Cliente com este id não encontrado");
        }
        customerInfoDto.setId(customerEntity.getId());
        customerInfoDto.setName(customerEntity.getNome());
        customerInfoDto.setBirth(customerEntity.getDataNascimento());
        customerInfoDto.setCpf(customerEntity.getCpf());
        customerInfoDto.setAddress(customerEntity.getEndereco());
        return customerInfoDto;
    }
}
