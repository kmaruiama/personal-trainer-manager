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
public class CustomerEditService {
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerEditService(ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer,
                               CustomerRepository customerRepository
    ) {
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.customerRepository = customerRepository;
    }

    public void execute(String authHeader, CustomerInfoDto customerInfoDto) throws Exception{
        if (!validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader), customerInfoDto.getId()))
        {
            throw new Exception("O treinador não possui permissão para este cliente.");
        }
        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(customerInfoDto.getId());
        if (optionalCustomerEntity.isPresent()) {
            CustomerEntity customerEntity = optionalCustomerEntity.get();
            checkingEditFields(customerInfoDto, customerEntity);
            customerRepository.save(customerEntity);
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
