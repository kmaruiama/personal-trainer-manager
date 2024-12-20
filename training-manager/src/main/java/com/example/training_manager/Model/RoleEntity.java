package com.example.training_manager.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "funcoes")
@Getter
@Setter
public class RoleEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    private String role;
}
