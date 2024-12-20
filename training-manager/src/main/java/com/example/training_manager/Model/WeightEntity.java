package com.example.training_manager.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "composicao")
public class WeightEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name= "data", nullable = false)
    private Date date;

    @Column
    private float height;

    @Column(name= "bf", nullable = false)
    private float bodyFatPercentage;

    @Column(name= "nascimento", nullable = false)
    private float weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private CustomerEntity customerEntity;
}
