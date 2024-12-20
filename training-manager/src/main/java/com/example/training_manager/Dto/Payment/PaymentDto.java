package com.example.training_manager.Dto.Payment;
import lombok.Data;
import java.util.Date;

@Data
public class PaymentDto {
    private Date dataVencimento;
    private float preco;
    private String modalidade;
    private Long customerId;
}
