package com.example.training_manager.Service.Payment;

import com.example.training_manager.Dto.Payment.PaymentGetDto;
import com.example.training_manager.Model.PaymentEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.PaymentRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchPaymentByCustomer {
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;

    @Autowired
    FetchPaymentByCustomer (PaymentRepository paymentRepository,
                            CustomerRepository customerRepository,
                            ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer){
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
    }

    public PaymentGetDto execute(String authHeader, long id) throws Exception{
        if(!validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader), id)){
            throw new Exception("O treinador não possui permissão para este cliente.");
        }

        PaymentEntity paymentEntity = paymentRepository.findPaymentEntityByCustomerId(id);
        if (paymentEntity == null){
            throw new Exception("Pagamento do cliente ainda não cadastrado");
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
