package com.example.training_manager.Dto.Customer;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerPricingRawDto {

    @Valid

    @NotNull(message="O nome é obrigatório")
    @NotBlank(message="O nome é obrigatório")
    private String name;

    @NotNull(message="O cpf é obrigatório")
    @NotBlank(message="O cpf é obrigatório")
    private String cpf;

    @NotNull(message="O endereço é obrigatório")
    @NotBlank(message="O endereço é obrigatório")
    private String address;

    @NotNull(message="A data de nascimento é obrigatória")
    @NotBlank(message="A data de nascimento é obrigatória")
    @Past(message = "Data de nascimento inválida")
    private String birth;

    @NotNull(message="O preço é obrigatório")
    @NotBlank(message="O preço é obrigatório")
    private float price;

    @Pattern(regexp = "Mensal|Semanal|Único", message = "Modalidade de pagamento inválida")
    @NotNull(message="O tipo de pagamento é obrigatório")
    @NotBlank(message="O tipo de pagamento é obrigatório")
    private String typeOfPayment;
}

