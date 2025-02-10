package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.ScheduleEntity;
import com.example.training_manager.Model.ScheduleMode;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.ScheduleRepository;
import com.example.training_manager.Repository.WorkoutRepository;
import com.example.training_manager.Service.Shared.ValidateToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ReturnNextWorkoutService {
    private final ValidateToken validateToken;
    private final CustomerRepository customerRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    ReturnNextWorkoutService(ValidateToken validateToken,
                             CustomerRepository customerRepository,
                             ScheduleRepository scheduleRepository,
                             WorkoutRepository workoutRepository) {
        this.validateToken = validateToken;
        this.customerRepository = customerRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public List<String> execute(Long customerId, String authHeader) {
        validateToken.execute(customerId, authHeader);
        CustomerEntity customer = getCustomerEntity(customerId);

        //é por dia da semana ou é por ordem?
        if (customer.getScheduleMode() == ScheduleMode.BY_ORDER) {
            return populateNextDtoListByOrder(customerId);
        }
        if (customer.getScheduleMode() == ScheduleMode.BY_DAY) {
            return populateNextDtoListByDay(customerId);
        }
        //se o enum nao caiu em nenhuma condição tem algo errado ai
        throw new CustomException.IrregularSchedulingEnumException("Enum irregular.");
    }

    private CustomerEntity getCustomerEntity(Long customerId) {
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(customerId);
        if (customerEntityOptional.isPresent()) {
            return customerEntityOptional.get();
        } else {
            throw new CustomException.CustomerNotFound("Cliente não encontrado");
        }
    }

    private List<String> populateNextDtoListByOrder(Long customerId) {
        List<String> workoutName = new ArrayList<>();
        List<ScheduleEntity> scheduleEntityList = scheduleRepository.findScheduleEntitiesByCustomerEntityId(customerId);
        if (scheduleEntityList.isEmpty()){
            throw new CustomException.ScheduleNotFoundException("Nenhum agendamento cadastrado");
        }
        sortByDayAndHour(scheduleEntityList);
        int i;
        //achar o primeiro schedule entity que nao foi concluido
        for (i = 0; i<scheduleEntityList.size(); i++){
            if(!scheduleEntityList.get(i).getDone()){
                workoutName.add(scheduleEntityList.get(i).getWorkoutEntity().getName());
                break;
            }
        }
        return workoutName;
    }

    // será retornado uma lista de strings pois é possível cadastrar dois treinos no mesmo dia
    private List<String> populateNextDtoListByDay(Long customerId) {
        List<String> workoutNameList = new ArrayList<>();
        int today = LocalDate.now().getDayOfWeek().getValue();
        workoutNameList.addAll(scheduleRepository.findWorkoutBasedInTheDayOfTheWeek(customerId, today));
        if (workoutNameList.isEmpty()) {
            throw new CustomException.ScheduleNotFoundException("Sem agendamentos para hoje.");
        }
        return workoutNameList;
    }

    private void sortByDayAndHour(List<ScheduleEntity> scheduleEntityList){
        scheduleEntityList.sort(Comparator
                .comparingInt(ScheduleEntity::getDayOfTheWeek)
                .thenComparing(ScheduleEntity::getHourStart));
    }
}
