package com.example.training_manager.Repository;

import com.example.training_manager.Model.ProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository extends JpaRepository<ProgramEntity, Long> {
    @Query("SELECT program from ProgramEntity program WHERE program.customerEntity.id = :id")
    Optional<ProgramEntity> ReturnProgramFromCustomerId(Long id);
}
