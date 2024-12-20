package com.example.training_manager.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "treinador")
public class TrainerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="nome", nullable = false)
    private String nome;
    @Column(name= "endereco", nullable = false, length = 300)
    private String endereco;

    @Column(name= "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name= "nascimento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @OneToMany(mappedBy = "id", cascade = CascadeType.REMOVE)
    private List<CustomerEntity> customerEntities = new ArrayList<>();

    @OneToMany(mappedBy = "id", cascade = CascadeType.REMOVE)
    private List<ScheduleEntity> schedule = new ArrayList<>();

    @OneToMany(mappedBy = "id", cascade = CascadeType.REMOVE)
    private List<PaymentEntity> paymentEntities = new ArrayList<>();

    @OneToOne(mappedBy = "trainer")
    private UserEntity userEntity;
}
