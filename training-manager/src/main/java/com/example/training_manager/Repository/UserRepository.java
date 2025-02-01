package com.example.training_manager.Repository;

import com.example.training_manager.Model.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Boolean existsByUsername(String username);
    @Query("SELECT u.trainerEntity.id FROM UserEntity u WHERE u.username = :username")
    Optional<Long> findTrainerIdByUsername(@Param("username") String username);
}
