package com.example.training_manager.Dto.Authentication;

import lombok.Data;

import java.util.Date;

@Data
public class TrainerDto {
    private String email;
    private String nome;
    private String endereco;
    private String cpf;
    private Date nascimento;
}
