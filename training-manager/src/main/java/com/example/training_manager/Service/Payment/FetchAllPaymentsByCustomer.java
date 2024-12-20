package com.example.training_manager.Service.Payment;

import com.example.training_manager.Dto.Payment.PaymentGetDto;
import com.example.training_manager.Model.PaymentEntity;
import com.example.training_manager.Repository.PaymentRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;

import java.util.ArrayList;
import java.util.List;

public class FetchAllPaymentsByCustomer {
    private final PaymentRepository paymentRepository;
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;

    FetchAllPaymentsByCustomer(PaymentRepository paymentRepository,
                            ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer) {
        this.paymentRepository = paymentRepository;
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
    }

    public List<PaymentGetDto> execute(String authHeader, Long id) throws Exception{
        if(!validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader), id)){
            throw new Exception("O treinador não possui permissão para este cliente.");
        }

        PaymentEntity paymentEntity = paymentRepository.findPaymentEntityByCustomerId(id);
        if (paymentEntity == null){
            throw new Exception("Pagamento do cliente ainda não cadastrado");
        }
        List<PaymentEntity> paymentEntityList = paymentRepository.findAllPaymentEntitiesByCustomer(id);
        return transformAllEntitiesIntoDTOs(paymentEntityList);
    }

    private List<PaymentGetDto> transformAllEntitiesIntoDTOs(List<PaymentEntity> scheduleEntityList){
        List<PaymentGetDto> paymentGetDtoList = new ArrayList<>();
        for(int i = 0; i< scheduleEntityList.size();i++){
            PaymentGetDto paymentGetDto = new PaymentGetDto();
            paymentGetDto.setPaymentType(scheduleEntityList.get(i).getModalidade());
            paymentGetDto.setPrice(scheduleEntityList.get(i).getPreco());
            paymentGetDto.setDueDate(scheduleEntityList.get(i).getDataVencimento());
            paymentGetDto.setPaymentId(scheduleEntityList.get(i).getId());
            paymentGetDtoList.add(paymentGetDto);
        }
        return paymentGetDtoList;
    }
}
