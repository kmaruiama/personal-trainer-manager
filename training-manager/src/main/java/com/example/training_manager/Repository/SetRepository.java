package com.example.training_manager.Repository;

import com.example.training_manager.Model.SetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SetRepository extends JpaRepository<SetEntity, Long> {
    @Query("SELECT sets FROM SetEntity sets WHERE sets.exerciseEntity.id = :id")
    List<SetEntity> returnAllSetsFromExerciseId(Long id);

    @Query("SELECT sets FROM SetEntity sets WHERE sets.customerEntity.id = :customerId AND sets.exerciseEntity.id = :exerciseId")
    List<SetEntity> returnAllSetFromExerciseBasedOnCustomerId(Long customerId, Long exerciseId);

    @Query("SELECT sets FROM SetEntity sets WHERE sets.exerciseEntity.workoutEntity.id = :workoutId")
    List<SetEntity> returnAllSetFromWorkoutBasedOnWorkoutId(Long workoutId);

}
