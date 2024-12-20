package com.example.training_manager.Controller;

import com.example.training_manager.Dto.Authentication.AuthResponseDto;
import com.example.training_manager.Dto.Authentication.LoginDto;
import com.example.training_manager.Dto.Authentication.RegisterTrainerRawDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Service.Authentication.AuthenticationService;
import com.example.training_manager.Service.Authentication.BreakRawRegisterJsonIntoTrainerAndUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
@CrossOrigin(origins = "http://localhost:8100")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final BreakRawRegisterJsonIntoTrainerAndUserService breakRawRegisterJsonIntoTrainerAndUserService;

    @Autowired
    public AuthController(AuthenticationService authenticationService,
                          BreakRawRegisterJsonIntoTrainerAndUserService breakRawRegisterJsonIntoTrainerAndUserService) {
        this.authenticationService = authenticationService;
        this.breakRawRegisterJsonIntoTrainerAndUserService = breakRawRegisterJsonIntoTrainerAndUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        try {
            return ResponseEntity.ok(authenticationService.login(loginDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterTrainerRawDto registerTrainerRawDto) {
        try {
            breakRawRegisterJsonIntoTrainerAndUserService.execute(registerTrainerRawDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Usuário registrado com sucesso."));
        } catch (CustomException.UsernameAlreadyExistsException | CustomException.CpfAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred during registration: " + e.getMessage()));
        }
    }
}
