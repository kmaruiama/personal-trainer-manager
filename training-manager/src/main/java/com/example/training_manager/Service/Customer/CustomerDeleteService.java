package com.example.training_manager.Service.Customer;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerDeleteService {
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerDeleteService(ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer,
                                 CustomerRepository customerRepository) {
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.customerRepository = customerRepository;
    }

    public void execute (String authHeader, Long id)throws Exception{
        if (!validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader), id))
        {
            throw new Exception("O treinador não possui permissão para este cliente.");
        }
        customerRepository.deleteById(id);
    }
}
