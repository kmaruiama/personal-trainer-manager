package com.example.training_manager.Service.Payment;

import com.example.training_manager.Dto.Payment.PaymentDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.PaymentEntity;
import com.example.training_manager.Repository.PaymentRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateToken;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentEditService {
    private final PaymentRepository paymentRepository;
    private final ValidateToken validateToken;

    @Autowired
    PaymentEditService( PaymentRepository paymentRepository, ValidateToken validateToken){
        this.paymentRepository = paymentRepository;
        this.validateToken = validateToken;
    }

    public void execute(PaymentDto paymentDto, String authHeader){
        validateToken.execute(paymentDto.getCustomerId(), authHeader);
        PaymentEntity paymentEntity = paymentRepository.findPaymentEntityByCustomerId(paymentDto.getCustomerId());
        if (paymentEntity == null){
            throw new CustomException.PaymentNotFoundException("Nenhum pagamento encontrado");
        }
        paymentEntity.setDataVencimento(paymentDto.getDataVencimento());
        paymentEntity.setModalidade(paymentDto.getModalidade());
        paymentEntity.setPreco(paymentDto.getPreco());
        paymentRepository.save(paymentEntity);
    }
}
