package com.example.training_manager.Service.Authentication;

import com.example.training_manager.Dto.Authentication.RegisterDto;
import com.example.training_manager.Dto.Authentication.TrainerDto;
import com.example.training_manager.Model.TrainerEntity;
import com.example.training_manager.Model.UserEntity;
import com.example.training_manager.Repository.TrainerRepository;
import com.example.training_manager.Repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class LinkTrainerToUserService {

    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Autowired
    public LinkTrainerToUserService(TrainerRepository trainerRepository, UserRepository userRepository, EntityManager entityManager) {
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    public void execute(TrainerDto trainerDto, RegisterDto registerDto) throws Exception {
        TrainerEntity trainer = trainerRepository.findByCpf(trainerDto.getCpf());
        Optional<UserEntity> userOptional = userRepository.findByUsername(registerDto.getUsername());
        if(userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setTrainer(trainer);
            entityManager.persist(user);
            entityManager.flush();// entender depois pq o userrepo nao faz o update
        } else throw new Exception("Usuário não encontrado");
    }
}
