package com.example.training_manager.Dto.Authentication;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterTrainerRawDto {

    private String username;
    private String password;
    private String email;
    private String name;
    private String address;
    private String cpf;
    private LocalDate birth;
}
