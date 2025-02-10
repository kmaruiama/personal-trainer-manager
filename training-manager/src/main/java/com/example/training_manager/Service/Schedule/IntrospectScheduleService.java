package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.ScheduleEntity;
import com.example.training_manager.Repository.ScheduleRepository;
import com.example.training_manager.Service.Shared.ValidateToken;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class IntrospectScheduleService {

    private final ValidateToken validateToken;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    IntrospectScheduleService(ValidateToken validateToken, ScheduleRepository scheduleRepository){
        this.validateToken = validateToken;
        this.scheduleRepository = scheduleRepository;
    }


    @Transactional
    public void execute(Long customerId, String authHeader){
        validateToken.execute(customerId, authHeader);
        List<ScheduleEntity> scheduleEntityList = scheduleRepository.findScheduleEntitiesByCustomerEntityId(customerId);
        if(scheduleEntityList.isEmpty()){
            throw new CustomException.ScheduleNotFoundException("Nenhum agendamento encontrado");
        }
        sortByDayAndHour(scheduleEntityList);
        introspectSchedule(scheduleEntityList);
    }

    private void introspectSchedule(List<ScheduleEntity> scheduleEntityList){
        //caso 1: só há um treino na agenda e ele já foi concluido
        if(scheduleEntityList.size() == 1){
            if (scheduleEntityList.getFirst().getDone()){
                scheduleEntityList.getFirst().setDone(false);
            }
        }
        //caso 2: um treino novo foi adicionado na agenda
        for (int i = 0; i<scheduleEntityList.size() - 1; i++){
            //size()-1 pq vai causar um indexoutofbounds quando tratando o ultimo caso
            //caso 2.1: um treino novo foi adicionado antes do proximo treino
            //ex: estou no treino da quarta, adicionei um novo treino no programa pra segunda
            if(!scheduleEntityList.get(i).getDone() && scheduleEntityList.get(i+1).getDone()){
                //se for essa a situacao, entao você só vai realizar esse novo treino semana que vem
                scheduleEntityList.get(i).setDone(true);
            }
        }
        //caso 3: há mais de um treino na agenda e todos foram concluidos
        boolean weekIsDone = true;
        for (int i = 0; i<scheduleEntityList.size(); i++){
            if (!scheduleEntityList.get(i).getDone()){
                weekIsDone = false;
            }
        }
        if (weekIsDone){
            for (int i = 0; i<scheduleEntityList.size(); i++){
                scheduleEntityList.get(i).setDone(false);
            }
        }
        scheduleRepository.saveAll(scheduleEntityList);
    }

    private void sortByDayAndHour(List<ScheduleEntity> scheduleEntityList){
        scheduleEntityList.sort(Comparator
                .comparingInt(ScheduleEntity::getDayOfTheWeek)
                .thenComparing(ScheduleEntity::getHourStart));
    }

}
