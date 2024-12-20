package com.example.training_manager.Service.Authentication;

import com.example.training_manager.Dto.Authentication.AuthResponseDto;
import com.example.training_manager.Dto.Authentication.LoginDto;
import com.example.training_manager.Dto.Authentication.RegisterDto;
import com.example.training_manager.Model.RoleEntity;
import com.example.training_manager.Model.UserEntity;
import com.example.training_manager.Repository.RoleRepository;
import com.example.training_manager.Repository.UserRepository;
import com.example.training_manager.Security.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 RoleRepository roleRepository,
                                 AuthenticationManager authenticationManager,
                                 PasswordEncoder passwordEncoder,
                                 TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
    }

    public AuthResponseDto login(LoginDto loginDto) throws Exception{
        String token;
        System.out.println(loginDto.getUsername());
        try {
            Optional<Long> trainerIdOptional = userRepository.findTrainerIdByUsername(loginDto.getUsername());
            String trainerId = trainerIdOptional.map(Object::toString).orElse(null);
            //isso é necessário para usar o jwt como indicador (trainer_id) das operacoes
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = tokenGenerator.generator(trainerId, authentication);
        } catch (Exception e) {
            throw new Exception("Senha e/ou usuário inválido");
        }
        return new AuthResponseDto(token, loginDto.getUsername());
    }

    public void register(RegisterDto registerDto) throws Exception{

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userEntity.setEmail(registerDto.getEmail());
        RoleEntity role = roleRepository.findByRole("ROLE_ADMIN")
                .orElseThrow(() -> new Exception ("Erro ao atribuir role"));
        userEntity.setRoles(Collections.singletonList(role));
        userRepository.save(userEntity);
    }

}
