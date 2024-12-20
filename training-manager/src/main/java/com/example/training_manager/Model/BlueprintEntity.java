package com.example.training_manager.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class BlueprintEntity {
    @OneToOne(cascade = CascadeType.ALL)
    ProgramEntity programEntity;
    @OneToOne(cascade = CascadeType.ALL)
    CustomerEntity customerEntity;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
}
