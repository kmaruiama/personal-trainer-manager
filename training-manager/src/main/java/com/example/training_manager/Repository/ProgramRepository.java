package com.example.training_manager.Repository;

import com.example.training_manager.Model.ProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProgramRepository extends JpaRepository<ProgramEntity, Long> {

    @Query("SELECT blueprint FROM ProgramEntity blueprint WHERE blueprint.customerEntity.id = :id AND blueprint.blueprint = true ORDER BY blueprint.id DESC")
    ProgramEntity findByCustomerIdAndBlueprintTrue(Long id);

    @Query("SELECT blueprint.customerEntity.id FROM ProgramEntity blueprint WHERE blueprint.id = :id")
    Long returnCustomerIdByProgramBlueprintId(Long id);
}
