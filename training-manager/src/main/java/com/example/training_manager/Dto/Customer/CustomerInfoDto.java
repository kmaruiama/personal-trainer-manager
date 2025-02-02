package com.example.training_manager.Dto.Customer;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class CustomerInfoDto {
    @Valid

    @NotNull(message="O nome é obrigatório")
    @NotBlank(message="O nome é obrigatório")
    private String name;

    @NotNull(message="O endereço é obrigatório")
    @NotBlank(message="O endereço é obrigatório")
    private String address;

    @NotNull(message="ID inválido")
    @Min(1)
    private Long id;

    @NotNull(message="A data de nascimento é obrigatória")
    @Past(message = "Data de nascimento inválida")
    private LocalDate birth;

    @NotNull(message="O cpf é obrigatório")
    @NotBlank(message="O cpf é obrigatório")
    private String cpf;
}
