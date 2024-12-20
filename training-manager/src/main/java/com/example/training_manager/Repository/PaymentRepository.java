package com.example.training_manager.Repository;

import com.example.training_manager.Model.PaymentEntity;
import com.example.training_manager.Model.ScheduleEntity;
import com.example.training_manager.Model.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    @Query("SELECT payment FROM PaymentEntity payment WHERE payment.customerEntity.id = :customerId ORDER BY payment.dataVencimento DESC")
    PaymentEntity findPaymentEntityByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT payment FROM PaymentEntity payment WHERE payment.trainerEntity.id = :trainerId ORDER BY payment.dataVencimento DESC")
    List<PaymentEntity> findPaymentEntitiesByTrainer(Long trainerId);

    @Query("SELECT payment FROM PaymentEntity payment WHERE payment.customerEntity.id = :customerId")
    List<PaymentEntity> findAllPaymentEntitiesByCustomer(Long customerId);
}
