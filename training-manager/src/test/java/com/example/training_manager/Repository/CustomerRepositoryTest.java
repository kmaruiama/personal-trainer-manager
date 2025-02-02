package com.example.training_manager.Repository;

import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.TrainerEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TrainerRepository trainerRepository;

    private Long trainerId;
    private Long customerId1;

    @BeforeEach
    void setCharacters(){
        LocalDate localDate = LocalDate.of(1000, 01, 01);
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setNome("Caio Otávio");
        trainerEntity.setCpf("123.456.789-00");
        trainerEntity.setEndereco("Roma, República Roma");
        trainerEntity.setDataNascimento(localDate);
        trainerRepository.save(trainerEntity);

        trainerId = trainerEntity.getId();

        CustomerEntity customerEntity1 = new CustomerEntity();
        customerEntity1.setNome("Alexandre O Grande");
        customerEntity1.setCpf("000.000.000-00");
        customerEntity1.setDataNascimento(localDate);
        customerEntity1.setEndereco("Pela, Macedônia");
        customerEntity1.setTrainerEntity(trainerEntity);
        customerRepository.save(customerEntity1);

        customerId1 = customerEntity1.getId();
    }

    @Test
    void mustReturnTheEntity() {
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(customerId1);
        Assertions.assertTrue(customerEntityOptional.isPresent());

        CustomerEntity customerEntity = customerEntityOptional.get();
        Assertions.assertEquals("Alexandre O Grande", customerEntity.getNome());
    }

    @Test
    void mustReturnTheCorrectName(){
        String name = customerRepository.findCustomerNameById(customerId1);
        Assertions.assertEquals("Alexandre O Grande", name);
    }

    @Test
    void testingExistsByTrainerIdAndCustomerIdInDifferentSituations (){
        Assertions.assertTrue(customerRepository.existsByTrainerIdAndCustomerId(trainerId, customerId1));
        Assertions.assertFalse(customerRepository.existsByTrainerIdAndCustomerId(3L, customerId1));
        Assertions.assertFalse(customerRepository.existsByTrainerIdAndCustomerId(trainerId, 3L));
    }

    @Test
    void testingExistsByCpfInDifferentSituations(){
        Assertions.assertTrue(customerRepository.existsByCpf("000.000.000-00"));
        Assertions.assertFalse(customerRepository.existsByCpf("0001029222"));
    }
}
