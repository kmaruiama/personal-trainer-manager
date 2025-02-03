package com.example.training_manager.Service.Payment;

import com.example.training_manager.Dto.Payment.PaymentGetDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.PaymentEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.PaymentRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateToken;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchPaymentByCustomer {
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final ValidateToken validateToken;

    @Autowired
    FetchPaymentByCustomer (PaymentRepository paymentRepository,
                            CustomerRepository customerRepository,
                            ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer, ValidateToken validateToken){
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.validateToken = validateToken;
    }

    public PaymentGetDto execute(String authHeader, long id){
        validateToken.execute(id, authHeader);

        PaymentEntity paymentEntity = paymentRepository.findPaymentEntityByCustomerId(id);
        if (paymentEntity == null){
            throw new CustomException.PaymentNotFoundException("Nenhum pagamento encontrado");
        }
        PaymentGetDto paymentGetDto = new PaymentGetDto();
        paymentGetDto.setPaymentType(paymentGetDto.getPaymentType());
        paymentGetDto.setPrice(paymentEntity.getPreco());
        paymentGetDto.setDueDate(paymentEntity.getDataVencimento());
        paymentGetDto.setPaymentId(paymentEntity.getId());
        paymentGetDto.setCustomerName(customerRepository.findCustomerNameById(id));
        return paymentGetDto;
    }
}
