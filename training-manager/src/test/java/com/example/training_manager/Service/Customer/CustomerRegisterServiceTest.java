package com.example.training_manager.Service.Customer;

import com.example.training_manager.Dto.Customer.CustomerPricingRawDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.TrainerEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.TrainerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CustomerRegisterServiceTest {

   /* TrainerEntity trainerEntity;

    @BeforeEach
    void setTrainer(){
        trainerEntity = new TrainerEntity();
        trainerEntity.setId(1L);
        trainerEntity.setCustomerEntities(new ArrayList<>());
    }

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private CustomerRegisterService customerRegisterService;

    @Test
    void mustCreateNewCustomer(){
        CustomerPricingRawDto customerPricingRawDto = new CustomerPricingRawDto();
        customerPricingRawDto.setPrice(100F);
        customerPricingRawDto.setName("Alarico I");
        customerPricingRawDto.setAddress("Ravena, Emília Romanha, Itália");
        customerPricingRawDto.setBirth(LocalDate.of(2000, 1, 1));
        customerPricingRawDto.setTypeOfPayment("Mensal");
        customerPricingRawDto.setCpf("000.000.000-01");

        when(customerRepository.existsByCpf(customerPricingRawDto.getCpf())).thenReturn(false);
        when(trainerRepository.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            if (id.equals(1L)) {
                return Optional.of(trainerEntity);
            }
            return Optional.empty();
        });

        boolean exceptionThatMustBeTrue = false;

        try {
            customerRegisterService.execute(customerPricingRawDto, "invalidString");
        } catch (CustomException.InvalidHeader invalidHeaderException){
            exceptionThatMustBeTrue = true;
        }
        Assertions.assertThat(exceptionThatMustBeTrue).isTrue();
        exceptionThatMustBeTrue = false;

    }*/
}