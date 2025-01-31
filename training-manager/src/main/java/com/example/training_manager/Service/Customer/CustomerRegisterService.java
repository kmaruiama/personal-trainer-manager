package com.example.training_manager.Service.Customer;

import com.example.training_manager.Dto.Customer.CustomerPricingRawDto;
import com.example.training_manager.Dto.Payment.PaymentDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.TrainerEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.TrainerRepository;
import com.example.training_manager.Service.Payment.AddPaymentService;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;


@Service
public class CustomerRegisterService {
    private final CustomerRepository customerRepository;
    private final TrainerRepository trainerRepository;
    private final AddPaymentService addPaymentService;

    @Autowired
    public CustomerRegisterService(CustomerRepository customerRepository,
                                   TrainerRepository trainerRepository,
                                   AddPaymentService addPaymentService
                                   ) {
        this.customerRepository = customerRepository;
        this.trainerRepository = trainerRepository;
        this.addPaymentService = addPaymentService;
    }

    @Transactional
    public void execute(CustomerPricingRawDto customerPricingRawDto, String authHeader) throws Exception {
        if (customerRepository.existsByCpf(customerPricingRawDto.getCpf())) {
            throw new CustomException.CpfAlreadyExistsException("O CPF já foi cadastrado.");
        }

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setNome(customerPricingRawDto.getName());
        customerEntity.setCpf(customerPricingRawDto.getCpf());
        customerEntity.setEndereco(customerPricingRawDto.getAddress());

        LocalDate localDate = LocalDate.parse(customerPricingRawDto.getBirth(), DateTimeFormatter.ISO_DATE);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        customerEntity.setDataNascimento(date);

        customerEntity.setTrainerEntity(linkCustomerToTrainerAndReturnTrainerToBeLinked(authHeader, customerEntity));
        //desnecessario com o transactional
        //customerRepository.save(customerEntity);

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setCustomerId(customerEntity.getId());
        paymentDto.setPreco(customerPricingRawDto.getPrice());
        paymentDto.setModalidade(customerPricingRawDto.getTypeOfPayment());
        setPaymentDate(paymentDto);

        addPaymentService.execute(authHeader, paymentDto);
    }


    private TrainerEntity linkCustomerToTrainerAndReturnTrainerToBeLinked(String authHeader, CustomerEntity customerEntity) throws Exception{
        Long trainerId = ReturnTrainerIdFromJWT.execute(authHeader);
        Optional <TrainerEntity> trainerEntityOptional = trainerRepository.findById(trainerId);
        if (trainerEntityOptional.isPresent()){
            TrainerEntity trainerEntity = trainerEntityOptional.get();
            trainerEntity.getCustomerEntities().add(customerEntity);
            return trainerEntity;
        }
        else {
            throw new Exception("treinador com esse id não encontrado");
        }
    }
    private void setPaymentDate(PaymentDto paymentDto){
        Date dueDate = Date.from(LocalDate.now()
                .plusMonths(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        paymentDto.setDataVencimento(dueDate);
    }
}
