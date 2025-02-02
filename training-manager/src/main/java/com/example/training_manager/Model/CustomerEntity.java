package com.example.training_manager.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="cliente")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="nome", nullable = false)
    private String nome;
    @Column(name= "endereco", nullable = false, length = 300)
    private String endereco;

    @Column(name= "cpf", nullable = false, unique = true) // vai dar problema se 2 treinadores distintos usarem o app e possuirem o mesmo cliente
    private String cpf;

    @Column(name= "nascimento", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "customerEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SetEntity> sets = new ArrayList<>();

    @OneToMany(mappedBy = "customerEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeightEntity> weightInput = new ArrayList<>();

    @OneToMany(mappedBy = "customerEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleEntity> schedule = new ArrayList<>();

    @OneToMany(mappedBy = "customerEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentEntity> payment = new ArrayList<>();

    @OneToMany(mappedBy = "customerEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramEntity> programEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "customerEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutEntity> workoutEntityList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private TrainerEntity trainerEntity;

    @OneToOne(mappedBy = "customerEntity")
    private BlueprintEntity blueprintEntity;
}
