package com.example.training_manager.Repository;

import com.example.training_manager.Model.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Long> {
    @Query("SELECT exercise FROM ExerciseEntity exercise WHERE exercise.workoutEntity.id = :id")
    List<ExerciseEntity> returnAllExerciseBlueprintsFromWorkoutId(Long id);
}

