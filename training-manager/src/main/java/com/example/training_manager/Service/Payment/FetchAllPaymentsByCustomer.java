package com.example.training_manager.Service.Payment;

import com.example.training_manager.Dto.Payment.PaymentGetDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.PaymentEntity;
import com.example.training_manager.Repository.PaymentRepository;
import com.example.training_manager.Service.Shared.ValidateToken;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FetchAllPaymentsByCustomer {
    private final PaymentRepository paymentRepository;
    private final ValidateToken validateToken;

    @Autowired
    FetchAllPaymentsByCustomer(PaymentRepository paymentRepository,
                               ValidateToken validateToken) {
        this.paymentRepository = paymentRepository;
        this.validateToken = validateToken;
    }

    public List<PaymentGetDto> execute(String authHeader, Long id) {
        validateToken.execute(id, authHeader);
        List<PaymentEntity> paymentEntityList = paymentRepository.findAllPaymentEntitiesByCustomer(id);
        if (paymentEntityList.isEmpty()) {
            throw new CustomException.PaymentNotFoundException("Nenhum pagamento encontrado");
        }
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
