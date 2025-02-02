package com.example.training_manager.Service.Customer;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Service.Shared.ValidateToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerDeleteService {
    private final ValidateToken validateToken;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerDeleteService(ValidateToken validateToken,
                                 CustomerRepository customerRepository) {
        this.validateToken = validateToken;
        this.customerRepository = customerRepository;
    }

    public void execute (String authHeader, Long id){
        validateToken.execute(id, authHeader);
        customerRepository.deleteById(id);
    }
}
