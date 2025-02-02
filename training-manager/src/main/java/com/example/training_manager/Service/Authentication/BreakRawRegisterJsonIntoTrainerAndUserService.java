package com.example.training_manager.Service.Authentication;

import com.example.training_manager.Dto.Authentication.RegisterTrainerRawDto;
import com.example.training_manager.Dto.Authentication.TrainerDto;
import com.example.training_manager.Dto.Authentication.RegisterDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Repository.TrainerRepository;
import com.example.training_manager.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BreakRawRegisterJsonIntoTrainerAndUserService {

    private final TrainerRegisterService trainerRegisterService;
    private final AuthenticationService authenticationService;
    private final LinkTrainerToUserService linkTrainerToUserService;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;

    @Autowired
    public BreakRawRegisterJsonIntoTrainerAndUserService(TrainerRegisterService trainerRegisterService,
                                                         AuthenticationService authenticationService,
                                                         LinkTrainerToUserService linkTrainerToUserService,
                                                         TrainerRepository trainerRepository,
                                                         UserRepository userRepository) {
        this.trainerRegisterService = trainerRegisterService;
        this.authenticationService = authenticationService;
        this.linkTrainerToUserService = linkTrainerToUserService;
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void execute(RegisterTrainerRawDto registerTrainerRawDto){
        if (trainerRepository.existsByCpf(registerTrainerRawDto.getCpf())) {
            throw new CustomException.CpfAlreadyExistsException("Um treinador com este cpf já foi cadastrado em nossa plataforma");
        }
        if (userRepository.existsByUsername(registerTrainerRawDto.getUsername())) {
            throw new CustomException.UsernameAlreadyExistsException("O username já está sendo usado");
        }

        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername(registerTrainerRawDto.getUsername());
        registerDto.setPassword(registerTrainerRawDto.getPassword());
        registerDto.setEmail(registerTrainerRawDto.getEmail());

        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setNome(registerTrainerRawDto.getName());
        trainerDto.setEndereco(registerTrainerRawDto.getAddress());
        trainerDto.setCpf(registerTrainerRawDto.getCpf());
        trainerDto.setNascimento(registerTrainerRawDto.getBirth());
        trainerRegisterService.register(trainerDto);
        authenticationService.register(registerDto);

        linkTrainerToUserService.execute(trainerDto, registerDto);
    }
}
