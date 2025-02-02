package com.example.training_manager.Dto.Authentication;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TrainerDto {
    private String email;
    private String nome;
    private String endereco;
    private String cpf;
    private LocalDate nascimento;
}
