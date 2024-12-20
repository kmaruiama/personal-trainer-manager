package com.example.training_manager.Service.Payment;

import com.example.training_manager.Dto.Payment.PaymentDto;
import com.example.training_manager.Model.PaymentEntity;
import com.example.training_manager.Repository.PaymentRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentEditService {
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final PaymentRepository paymentRepository;

    @Autowired
    PaymentEditService(ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer,
                       PaymentRepository paymentRepository){
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.paymentRepository = paymentRepository;
    }

    public void execute(PaymentDto paymentDto, String authHeader) throws Exception{
        if (!validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader), paymentDto.getCustomerId())){
            throw new Exception ("O treinador não possui permissão para este cliente.");
        }
        PaymentEntity paymentEntity = paymentRepository.findPaymentEntityByCustomerId(paymentDto.getCustomerId());
        if (paymentEntity == null){
            throw new Exception("Pagamento não encontrado no sistema.");
        }
        paymentEntity.setDataVencimento(paymentDto.getDataVencimento());
        paymentEntity.setModalidade(paymentDto.getModalidade());
        paymentEntity.setPreco(paymentDto.getPreco());
        paymentRepository.save(paymentEntity);
    }
}
