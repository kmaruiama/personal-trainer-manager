package com.example.training_manager.Repository;

import com.example.training_manager.Model.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<WorkoutEntity, Long> {
    WorkoutEntity findByName (String name);

    @Query("SELECT workout FROM WorkoutEntity workout WHERE workout.programEntity.id = :id")
    List<WorkoutEntity> returnAllWorkoutBlueprintsFromProgramById(Long id);

    //ruim e vai deixar o pedido muito lento conforme mais treinos forem inseridos
    //ver alguma coisa pra limitar
    @Query("SELECT workout FROM WorkoutEntity workout WHERE workout.customerEntity.id = :id ORDER BY workout.id DESC")
    List<WorkoutEntity> returnWorkoutsDescendant(Long id);

    //tive fazer essa gambiarra com nested query pq o jpql nao suporta o TOP clause
    @Query("SELECT workoutEntity FROM WorkoutEntity workoutEntity WHERE workoutEntity.id = (SELECT MAX(workoutEntity.id) FROM WorkoutEntity workoutEntity WHERE workoutEntity.customerEntity.id = :customerId)")
    WorkoutEntity returnLastWorkoutDone(Long customerId);


}
