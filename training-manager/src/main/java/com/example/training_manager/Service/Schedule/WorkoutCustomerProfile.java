package com.example.training_manager.Service.Schedule;


import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.WorkoutEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.WorkoutRepository;
import com.example.training_manager.Service.Shared.ValidateToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutCustomerProfile {
    private final ValidateToken validateToken;
    FetchScheduleByTrainer fetchScheduleByTrainer;
    CustomerRepository customerRepository;
    WorkoutRepository workoutRepository;

    @Autowired
    WorkoutCustomerProfile(FetchScheduleByTrainer fetchScheduleByTrainer,
                           CustomerRepository customerRepository,
                           WorkoutRepository workoutRepository, ValidateToken validateToken){
        this.fetchScheduleByTrainer = fetchScheduleByTrainer;
        this.customerRepository = customerRepository;
        this.workoutRepository = workoutRepository;
        this.validateToken = validateToken;
    }

    public List<String> execute(String authHeader, Long id){
        validateToken.execute(id, authHeader);
        return getLastThreeWorkoutsIfTheyExist(id, new ArrayList<>());
    }

    //ao inves de usar um boolean como nas implementacoes passadas, vou usar o cadastro no programa de treinos
    //como referencial, já que pra gerar relatórios e acertar a ordem o sistema independe dele
    //(portanto, se ta no programa é um blueprint e nao um treino q aconteceu de fato)
    private List<String> getLastThreeWorkoutsIfTheyExist(Long id, List<String> workoutNames) {
        List<WorkoutEntity> workoutEntityList = workoutRepository.returnWorkoutsDescendant(id, Pageable.ofSize(3));
        for (int i = 0; i<workoutEntityList.size(); i++){
            if (workoutEntityList.get(i).getName().isBlank()){
                workoutNames.add("Nulo");
            }
            else {
                String workoutName = workoutEntityList.get(i).getName();
                workoutNames.add(workoutName);
            }
        }
        while (workoutNames.size()<3){
            workoutNames.add("Nulo");
        }
        return workoutNames;
    }


}
