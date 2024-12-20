package com.example.training_manager.Repository;

import com.example.training_manager.Model.ScheduleEntity;
import com.example.training_manager.Model.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    @Query("SELECT schedule FROM ScheduleEntity schedule WHERE schedule.trainerEntity = :trainer")
    List<ScheduleEntity> findScheduleEntitiesByTrainer(TrainerEntity trainer);

    @Query("SELECT scheduleEntity.customerEntity.id FROM ScheduleEntity scheduleEntity WHERE scheduleEntity.id = :id")
    Long findCustomerIdByScheduleId(Long id);
}
