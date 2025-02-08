package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Dto.Workout.NextWorkoutDto;
import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Model.CustomerEntity;
import com.example.training_manager.Model.ScheduleEntity;
import com.example.training_manager.Model.ScheduleMode;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.ScheduleRepository;
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

    @Autowired
    ReturnNextWorkoutService(ValidateToken validateToken, CustomerRepository customerRepository, ScheduleRepository scheduleRepository){
        this.validateToken = validateToken;
        this.customerRepository = customerRepository;
        this.scheduleRepository = scheduleRepository;
    }
    public List<NextWorkoutDto> execute(Long customerId, String authHeader) {
        validateToken.execute(customerId, authHeader);
        CustomerEntity customer = getCustomerEntity(customerId);
        List<NextWorkoutDto> nextWorkoutDtoList = new ArrayList<>();
        //é por dia da semana ou é por ordem?
        if (customer.getScheduleMode() == ScheduleMode.BY_ORDER){
            populateNextDtoListByOrder(nextWorkoutDtoList);
        }
        if (customer.getScheduleMode() == ScheduleMode.BY_DAY){
            populateNextDtoListByDay(customerId, nextWorkoutDtoList);
        }
        return nextWorkoutDtoList;
    }

    private CustomerEntity getCustomerEntity (Long customerId) {
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(customerId);
        if (customerEntityOptional.isPresent()){
            return customerEntityOptional.get();
        }
        else throw new CustomException.CustomerNotFound("Cliente não encontrado");
    }

    private void populateNextDtoListByOrder(List<NextWorkoutDto> nextWorkoutDtoList){
        //preciso implementar isso aqui
    }
    //por enquanto o sistema nao permite 2 treinos no mesmo dia, preciso mudar isso ASAP!
    private void populateNextDtoListByDay(Long customerId, List<NextWorkoutDto> nextWorkoutDtoList) {
        /* int dayOfTheWeek = LocalDate.now().getDayOfWeek().getValue();
        String workoutName = scheduleRepository.findAllWorkoutsBasedInTheDayOfTheWeek(customerId, dayOfTheWeek);
        if (scheduleEntityList.isEmpty()){
            throw new CustomException.NoScheduleFoundException("Nenhum agendamento encontrado");
        }
        scheduleEntityList.stream().map()*/
    }
}
