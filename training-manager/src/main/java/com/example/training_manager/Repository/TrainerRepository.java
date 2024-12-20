package com.example.training_manager.Repository;

import com.example.training_manager.Model.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<TrainerEntity, Long>{

    boolean existsByCpf (String cpf);
    TrainerEntity findByCpf(String cpf);
}
