package com.example.training_manager.Repository;

import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrainerRepository extends JpaRepository<TrainerEntity, Long>{

    boolean existsByCpf (String cpf);
    TrainerEntity findByCpf(String cpf);

    @Query("SELECT c FROM CustomerEntity c WHERE c.trainerEntity.id = :id")
    List<CustomerEntity> findByCustomerEntitiesByTrainerEntityId(Long id);
}
