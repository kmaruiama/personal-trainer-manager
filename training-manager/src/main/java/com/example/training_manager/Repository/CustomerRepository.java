package com.example.training_manager.Repository;

import com.example.training_manager.Model.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository <CustomerEntity, Long> {
    boolean existsByCpf(String cpf);

    //porque eu decidi colocar essa implementacao aqui lmao
    //@Query("SELECT c FROM CustomerEntity c WHERE c.trainerEntity.id = :trainerId")
    //List<CustomerEntity> findCustomerEntitiesByTrainerId(@Param("trainerId") Long trainerId);

    @Query("SELECT c.nome FROM CustomerEntity c WHERE c.id = :id")
    String findCustomerNameById(@Param("id") Long id);


    @Query("SELECT CASE WHEN COUNT(c)>0 THEN TRUE ELSE FALSE END FROM CustomerEntity c WHERE c.trainerEntity.id = :trainerId AND c.id = :customerId")
    boolean existsByTrainerIdAndCustomerId(@Param("trainerId") Long trainerId, @Param("customerId") Long customerId);
}
