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


    //nova implementacao usando pivos pra ficar mais simples de entender

    //caso 1: pivô falso
    // D D D N D D N N
    // nesse caso, i[3] é o pivô falso e i[6] é o pivô real

    private void introspectSchedule(List<ScheduleEntity> scheduleEntityList){
        boolean pivotFound = false;
        int pivotPosition = 0;

        for (int i = 0; i<scheduleEntityList.size(); i++) {
            //achamos o pivo                             //está aqui pro caso 1
            if (!scheduleEntityList.get(i).getDone() && (i+1 == scheduleEntityList.size() || !scheduleEntityList.get(i).getDone()))
            {
                pivotFound = true;
                pivotPosition = i;
                break;
            }
        }
        if(pivotFound) {
            //limpando a lista, tudo o que vem antes do pivo foi treinado e tudo o que vem depois é falso
            for (int i = 0; i < scheduleEntityList.size(); i++) {
                if (i < pivotPosition) {
                    scheduleEntityList.get(i).setDone(true);
                } else {
                    scheduleEntityList.get(i).setDone(false);
                }
            }
        }
        //lista cheia
        else{
            for (int i = 0; i < scheduleEntityList.size(); i++){
                scheduleEntityList.get(i).setDone(false);
            }
        }
    }

    private void sortByDayAndHour(List<ScheduleEntity> scheduleEntityList){
        scheduleEntityList.sort(Comparator
                .comparingInt(ScheduleEntity::getDayOfTheWeek)
                .thenComparing(ScheduleEntity::getHourStart));
    }

}
