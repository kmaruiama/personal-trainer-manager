package com.example.training_manager.Service.Customer;

import com.example.training_manager.Dto.Customer.CustomerListGetDto;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FetchCustomersByTrainer {
    private final CustomerRepository customerRepository;

    @Autowired
    public FetchCustomersByTrainer(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerListGetDto> execute (String authHeader) throws Exception{
        List<CustomerEntity> customerEntityList = customerRepository.findCustomerNamesAndIdByTrainerId(ReturnTrainerIdFromJWT.execute(authHeader));
        List<CustomerListGetDto> customerListGetDtoList = new ArrayList<>();
        for(int i = 0;i<customerEntityList.size(); i++){
            CustomerListGetDto customerListGetDto = new CustomerListGetDto();
            customerListGetDto.setId(customerEntityList.get(i).getId());
            customerListGetDto.setName(customerEntityList.get(i).getNome());
            customerListGetDtoList.add(customerListGetDto);
        }
        return customerListGetDtoList;
    }
}
