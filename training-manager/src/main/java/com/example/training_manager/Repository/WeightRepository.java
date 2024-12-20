package com.example.training_manager.Repository;

import com.example.training_manager.Model.WeightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WeightRepository extends JpaRepository<WeightEntity, Long> {
    @Query("SELECT weight FROM WeightEntity weight WHERE weight.customerEntity.id = :id")
    List<WeightEntity> findWeightEntitiesByCustomerId(Long id);

    @Query("SELECT weight.customerEntity.id FROM WeightEntity weight WHERE weight.id = :id")
    Long findCustomerIdByWeightId(Long id);

    WeightEntity findTopByCustomerEntityIdOrderByDateDesc(Long id);

    Optional<WeightEntity> findById(Long id);
}
