package com.example.training_manager.Dto.Payment;
import lombok.Data;
import java.util.Date;

@Data
public class PaymentGetDto {
    private Long paymentId;
    private float price;
    private Date dueDate;
    private String paymentType;
    private String customerName;

}
