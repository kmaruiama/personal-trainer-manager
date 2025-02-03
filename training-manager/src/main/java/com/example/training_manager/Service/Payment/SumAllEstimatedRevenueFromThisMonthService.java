package com.example.training_manager.Service.Payment;

import com.example.training_manager.Model.PaymentEntity;
import com.example.training_manager.Repository.PaymentRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SumAllEstimatedRevenueFromThisMonthService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public SumAllEstimatedRevenueFromThisMonthService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Float execute(String authHeader){
        Long trainerId = ReturnTrainerIdFromJWT.execute(authHeader);
        List<PaymentEntity> paymentEntityList = paymentRepository.findFuturePaymentEntitiesByCustomer(trainerId);
        filterThisMonthPaymentEntities(paymentEntityList);
        return calculateMonthlyRevenue(paymentEntityList);
    }

    private void filterThisMonthPaymentEntities(List<PaymentEntity> paymentEntityList){
        YearMonth currentMonth = YearMonth.now();

        for (int i = 0; i < paymentEntityList.size(); i++) {
            PaymentEntity payment = paymentEntityList.get(i);
            LocalDate dueDate = convertToLocalDate(payment.getDataVencimento());
            YearMonth paymentMonth = YearMonth.from(dueDate);
            if (!paymentMonth.equals(currentMonth)) {
                paymentEntityList.remove(paymentEntityList.get(i));
                i--;
            }
        }
    }

    private Float calculateMonthlyRevenue(List<PaymentEntity> paymentEntityList) {
        Float totalRevenue = 0f;
        for (int i = 0; i < paymentEntityList.size(); i++) {
            totalRevenue += paymentEntityList.get(i).getPreco();
        }
        return totalRevenue;
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }
}
