package com.example.training_manager.Dto.Customer;


import lombok.Data;

import java.util.Date;

@Data
public class CustomerInfoDto {
    private String name;
    private int age;
    private String address;
    private Long id;
    private Date birth;
    private String cpf;

}
