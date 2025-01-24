package com.example.training_manager.Service.Payment;

import com.example.training_manager.Dto.Payment.PaymentDto;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.PaymentEntity;
import com.example.training_manager.Model.TrainerEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.PaymentRepository;
import com.example.training_manager.Repository.TrainerRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class AddPaymentService {
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final TrainerRepository trainerRepository;

    private final AddFuturePaymentIfTheClientAlreadyPayed addFuturePaymentIfTheClientAlreadyPayed;

    @Autowired
    AddPaymentService(ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer,
                      @Lazy AddFuturePaymentIfTheClientAlreadyPayed addFuturePaymentIfTheClientAlreadyPayed,
                      PaymentRepository paymentRepository,
                      CustomerRepository customerRepository,
                      TrainerRepository trainerRepository) {
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
        this.trainerRepository = trainerRepository;
        this.addFuturePaymentIfTheClientAlreadyPayed = addFuturePaymentIfTheClientAlreadyPayed;
    }

    public void execute(String authHeader, PaymentDto paymentDto) throws Exception {
        if (!validateTrainerOwnershipOverCustomer.execute(
                ReturnTrainerIdFromJWT.execute(authHeader), paymentDto.getCustomerId())) {
            throw new Exception("O treinador não possui permissão para este cliente.");
        }
        PaymentEntity paymentEntity = new PaymentEntity();
        setCustomer(paymentEntity, paymentDto);
        setTrainer(paymentEntity, authHeader);
        paymentEntity.setModalidade(paymentDto.getModalidade());
        paymentEntity.setPreco(paymentDto.getPreco());
        paymentEntity.setDataVencimento(paymentDto.getDataVencimento());
        paymentEntity.setPayed(paymentDto.isPayed());
        paymentRepository.save(paymentEntity);
        if(paymentDto.isPayed()) {
            addFuturePaymentIfTheClientAlreadyPayed.execute(paymentDto, authHeader);
        }
    }

    private void setCustomer(PaymentEntity paymentEntity, PaymentDto paymentDto) {
        Optional<CustomerEntity> customerEntityOptional = customerRepository
                .findById(paymentDto.getCustomerId());
        if (customerEntityOptional.isPresent()) {
            paymentEntity.setCustomerEntity(customerEntityOptional.get());
        }
    }

    private void setTrainer(PaymentEntity paymentEntity, String authHeader) throws Exception {
        Optional<TrainerEntity> trainerEntityOptional = trainerRepository
                .findById(
                        ReturnTrainerIdFromJWT.execute(
                                authHeader));
        if (trainerEntityOptional.isPresent()) {
            paymentEntity.setTrainerEntity(trainerEntityOptional.get());
        }
    }
}
