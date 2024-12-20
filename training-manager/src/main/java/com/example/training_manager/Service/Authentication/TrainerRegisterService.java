package com.example.training_manager.Service.Authentication;

import com.example.training_manager.Dto.Authentication.TrainerDto;
import com.example.training_manager.Model.TrainerEntity;
import com.example.training_manager.Repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TrainerRegisterService {
    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainerRegisterService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public void register(TrainerDto trainerDto) throws Exception {
        if (trainerRepository.existsByCpf(trainerDto.getCpf())) {
            throw new Exception("O CPF já está cadastrado");
        }
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setCpf(trainerDto.getCpf());
        trainerEntity.setNome(trainerDto.getNome());
        trainerEntity.setEndereco(trainerDto.getEndereco());
        trainerEntity.setDataNascimento(trainerDto.getNascimento());
        trainerEntity.setCustomerEntities(new ArrayList<>());

        trainerRepository.save(trainerEntity);
    }
}
