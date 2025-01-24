package com.example.training_manager.Service.Payment;

import com.example.training_manager.Dto.Payment.PaymentGetDto;
import com.example.training_manager.Model.PaymentEntity;
import com.example.training_manager.Repository.PaymentRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FetchPaymentsByTrainer {
    private final PaymentRepository paymentRepository;

    @Autowired
    FetchPaymentsByTrainer(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    public List<PaymentGetDto> execute(String authHeader) throws Exception {
        List<PaymentEntity> paymentEntityList = paymentRepository.findPaymentEntitiesByTrainer(
                (ReturnTrainerIdFromJWT.execute
                        (authHeader)));
        List<PaymentGetDto> paymentGetDtoList = new ArrayList<>();
        transformEntitiesIntoDtos(paymentGetDtoList, paymentEntityList);
        return paymentGetDtoList;
    }

    private void transformEntitiesIntoDtos(List<PaymentGetDto> paymentGetDtoList, List<PaymentEntity> paymentEntityList){
        for (int i = 0; i<paymentEntityList.size(); i++){
            PaymentGetDto paymentGetDto = new PaymentGetDto();
            paymentGetDto.setCustomerName(paymentEntityList.get(i).getCustomerEntity().getNome());
            paymentGetDto.setPaymentType(paymentEntityList.get(i).getModalidade());
            paymentGetDto.setPrice(paymentEntityList.get(i).getPreco());
            paymentGetDto.setDueDate(paymentEntityList.get(i).getDataVencimento());
            paymentGetDto.setPaymentId(paymentEntityList.get(i).getId());
            paymentGetDto.setPayed(paymentEntityList.get(i).isPayed());
            paymentGetDtoList.add(paymentGetDto);
        }
    }
}
