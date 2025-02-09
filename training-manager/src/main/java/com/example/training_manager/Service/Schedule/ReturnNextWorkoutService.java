package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.ScheduleEntity;
import com.example.training_manager.Model.ScheduleMode;
import com.example.training_manager.Model.WorkoutEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.ScheduleRepository;
import com.example.training_manager.Repository.WorkoutRepository;
import com.example.training_manager.Service.Shared.ValidateToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReturnNextWorkoutService {
    private final ValidateToken validateToken;
    private final CustomerRepository customerRepository;
    private final ScheduleRepository scheduleRepository;
    private final WorkoutRepository workoutRepository;

    @Autowired
    ReturnNextWorkoutService(ValidateToken validateToken,
                             CustomerRepository customerRepository,
                             ScheduleRepository scheduleRepository,
                             WorkoutRepository workoutRepository){
        this.validateToken = validateToken;
        this.customerRepository = customerRepository;
        this.scheduleRepository = scheduleRepository;
        this.workoutRepository = workoutRepository;
    }
    public List<String> execute(Long customerId, String authHeader) {
        validateToken.execute(customerId, authHeader);
        CustomerEntity customer = getCustomerEntity(customerId);
        List<String> workoutNameList = new ArrayList<>();
        //é por dia da semana ou é por ordem?
        if (customer.getScheduleMode() == ScheduleMode.BY_ORDER){
            populateNextDtoListByOrder(customerId, workoutNameList);
        }
        if (customer.getScheduleMode() == ScheduleMode.BY_DAY){
            populateNextDtoListByDay(customerId, workoutNameList);
        }
        return workoutNameList;
    }

    private CustomerEntity getCustomerEntity (Long customerId) {
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(customerId);
        if (customerEntityOptional.isPresent()){
            return customerEntityOptional.get();
        }
        else throw new CustomException.CustomerNotFound("Cliente não encontrado");
    }

    //esse bloco funciona comparando o nome do ultimo treino realizado com a lista de treinos em ordem
    //ele procura o ultimo treino realizado (i) na lista de agendamentos e retorna o proximo (i+1)

    //em caso de o cliente escolher os treinos by order, uma lista de apenas um node será retornada
    private void populateNextDtoListByOrder(Long customerId, List<String> workoutNameList){
        WorkoutEntity lastWorkout = workoutRepository.returnLastWorkoutDone(customerId);
        if (lastWorkout == null){
            throw new CustomException.WorkoutNotFoundException("Último treino não encontrado");
        }
        List<ScheduleEntity> workoutOrderList = scheduleRepository.findScheduleEntitiesByCustomerEntityId(customerId);
        if (workoutOrderList.isEmpty()){
            throw new CustomException.ScheduleNotFoundException("Nenhum agendamento encontrado");
        }
        for(int i = 0; i<workoutOrderList.size(); i++){
            //se o nome do treino for igual ao nome do ultimo treino, entao retornamos o proximo
            // a ser treinado( i + 1)
            if(workoutOrderList.get(i).getWorkoutEntity().getName().equals(lastWorkout.getName())){
                //se nao estamos no ultimo node da lista, retorna node + 1
                if (i != workoutOrderList.size() - 1) {
                    workoutNameList.add(workoutNameList.get(i + 1));
                }
                //se estamos, entao o proximo é o primeiro
                else{
                    workoutNameList.add(workoutOrderList.getFirst().getWorkoutEntity().getName());
                }
            }
        }
    }

    //será retornado uma lista de strings pois é possível cadastrar dois treinos no mesmo dia
    private void populateNextDtoListByDay(Long customerId, List<String> workoutNameList) {
        int today = LocalDate.now().getDayOfWeek().getValue();
        workoutNameList.addAll(scheduleRepository.findWorkoutBasedInTheDayOfTheWeek(customerId, today));
        if (workoutNameList.isEmpty()){
            throw new CustomException.ScheduleNotFoundException("Sem agendamentos para hoje.");
        }
    }
}
