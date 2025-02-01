package com.example.training_manager.Service.Customer;

import com.example.training_manager.Dto.Customer.CustomerInfoDto;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateToken;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerEditService {
    private final ValidateToken validateToken;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerEditService(ValidateToken validateToken,
                               CustomerRepository customerRepository
    ) {
        this.validateToken = validateToken;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public void execute(String authHeader, CustomerInfoDto customerInfoDto){
        validateToken.execute(customerInfoDto.getId(), authHeader);
        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(customerInfoDto.getId());
        if (optionalCustomerEntity.isPresent()) {
            CustomerEntity customerEntity = optionalCustomerEntity.get();
            checkingEditFields(customerInfoDto, customerEntity);
        }
    }

    private void checkingEditFields(CustomerInfoDto customerInfoDto, CustomerEntity customerEntity){
        if(customerInfoDto.getBirth()!= null){
            customerEntity.setDataNascimento(customerInfoDto.getBirth());
        }
        if(customerInfoDto.getName()!=null){
            customerEntity.setNome(customerInfoDto.getName());
        }
        if(customerInfoDto.getAddress()!=null){
            customerEntity.setEndereco(customerInfoDto.getAddress());
        }
        if(customerInfoDto.getCpf()!=null){
            customerEntity.setCpf(customerInfoDto.getCpf());
        }
    }
}
