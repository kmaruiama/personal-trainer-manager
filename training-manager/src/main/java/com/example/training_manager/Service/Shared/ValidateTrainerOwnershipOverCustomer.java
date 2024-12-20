package com.example.training_manager.Service.Shared;

import com.example.training_manager.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateTrainerOwnershipOverCustomer {
    private final CustomerRepository customerRepository;

    @Autowired
    public ValidateTrainerOwnershipOverCustomer(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public boolean execute(long trainerId, long customerId) {
        return customerRepository.existsByTrainerIdAndCustomerId(trainerId, customerId);
    }
}
