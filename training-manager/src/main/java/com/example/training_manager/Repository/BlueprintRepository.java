package com.example.training_manager.Repository;

import com.example.training_manager.Model.BlueprintEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlueprintRepository extends JpaRepository <BlueprintEntity, Long> {

}
