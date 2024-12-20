package com.example.training_manager.Dto.Customer;


import lombok.Data;

import java.util.Date;

@Data
public class CustomerPricingRawDto {
    private String name;
    private String cpf;
    private String address;
    private String birth;
    private float price;
    private String typeOfPayment;
}

