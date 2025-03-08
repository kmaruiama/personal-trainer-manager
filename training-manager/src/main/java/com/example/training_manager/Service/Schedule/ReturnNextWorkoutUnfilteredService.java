package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Dto.Schedule.NextWorkoutDto;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ReturnNextWorkoutUnfilteredService {
    private final ValidateToken validateToken;
    private final CustomerRepository customerRepository;
    private final ScheduleRepository scheduleRepository;
    private final WorkoutRepository workoutRepository;

    @Autowired
    ReturnNextWorkoutUnfilteredService(ValidateToken validateToken,
                                       CustomerRepository customerRepository,
                                       ScheduleRepository scheduleRepository,
                                       WorkoutRepository workoutRepository) {
        this.validateToken = validateToken;
        this.customerRepository = customerRepository;
        this.scheduleRepository = scheduleRepository;
        this.workoutRepository = workoutRepository;
    }

    public List<NextWorkoutDto> execute(Long customerId, String authHeader) {
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

    private List<NextWorkoutDto> populateNextDtoListByOrder(Long customerId) {
        List<NextWorkoutDto> nextWorkoutDtoList = new ArrayList<>();
        List<ScheduleEntity> scheduleEntityList = scheduleRepository.findScheduleEntitiesByCustomerEntityId(customerId);
        if (scheduleEntityList.isEmpty()){
            throw new CustomException.ScheduleNotFoundException("Nenhum agendamento cadastrado");
        }
        sortByDayAndHour(scheduleEntityList);
        int i;
        //achar o primeiro schedule entity que nao foi concluido
        for (i = 0; i<scheduleEntityList.size(); i++){
            if(!scheduleEntityList.get(i).getDone()){
                NextWorkoutDto nextWorkoutDtoUsingBlueprintAsReference = new NextWorkoutDto();
                nextWorkoutDtoUsingBlueprintAsReference.setName(scheduleEntityList.get(i).getWorkoutEntity().getName());
                nextWorkoutDtoUsingBlueprintAsReference.setId(scheduleEntityList.get(i).getWorkoutEntity().getId());
                nextWorkoutDtoUsingBlueprintAsReference.setBlueprint(true);
                nextWorkoutDtoList.add(nextWorkoutDtoUsingBlueprintAsReference);
                NextWorkoutDto nextWorkoutDtoUsingLastWorkoutAsReference = new NextWorkoutDto();
                nextWorkoutDtoUsingLastWorkoutAsReference.setId(findWorkoutIdFromLastWorkoutWithThisName(customerId, nextWorkoutDtoList.getFirst().getName(), scheduleEntityList.size()));
                nextWorkoutDtoUsingLastWorkoutAsReference.setName(scheduleEntityList.get(i).getWorkoutEntity().getName());
                nextWorkoutDtoUsingLastWorkoutAsReference.setBlueprint(false);
                if (nextWorkoutDtoUsingLastWorkoutAsReference.getId() != null){
                    nextWorkoutDtoList.add(nextWorkoutDtoUsingLastWorkoutAsReference);
                }
                break;
            }
        }
        return nextWorkoutDtoList;
    }

    private Long findWorkoutIdFromLastWorkoutWithThisName(Long customerId, String workoutName, int scheduleSize){
        //retornamos a quantidade minima de treinos em que está garantido que o treino workoutName
        //esteja contido em
        List<WorkoutEntity> workoutEntityList = workoutRepository.returnWorkoutsDescendant(customerId, Pageable.ofSize(scheduleSize));
        for (int i = 0; i<workoutEntityList.size(); i++){
            if (workoutEntityList.get(i).getName().equals(workoutName)){
                return workoutEntityList.get(i).getId();
            }
        }
        return null;
    }

    // será retornado uma lista de treinos pois é possível cadastrar dois treinos no mesmo dia
    private List<NextWorkoutDto> populateNextDtoListByDay(Long customerId) {
        List<NextWorkoutDto> nextWorkoutDtoList = new ArrayList<>();
        int today = LocalDate.now().getDayOfWeek().getValue();
        List<ScheduleEntity> scheduleEntityList = scheduleRepository.findScheduleEntityBasedOnTheDayOfTheWeek(customerId, today);
        if (scheduleEntityList.isEmpty()) {
            throw new CustomException.ScheduleNotFoundException("Sem agendamentos para hoje.");
        }
        for (int i = 0; i < scheduleEntityList.size(); i++){
            NextWorkoutDto nextWorkoutDtoUsingBlueprintAsReference = new NextWorkoutDto();
            nextWorkoutDtoUsingBlueprintAsReference.setName(scheduleEntityList.get(i).getWorkoutEntity().getName());
            nextWorkoutDtoUsingBlueprintAsReference.setId(scheduleEntityList.get(i).getWorkoutEntity().getId());
            nextWorkoutDtoUsingBlueprintAsReference.setBlueprint(true);
            nextWorkoutDtoList.add(nextWorkoutDtoUsingBlueprintAsReference);
            NextWorkoutDto nextWorkoutDtoUsingLastWorkoutAsReference = new NextWorkoutDto();
            nextWorkoutDtoUsingLastWorkoutAsReference.setName(scheduleEntityList.get(i).getWorkoutEntity().getName());
            nextWorkoutDtoUsingLastWorkoutAsReference.setId(findWorkoutIdFromLastWorkoutWithThisName(customerId, nextWorkoutDtoList.getFirst().getName(), scheduleEntityList.size()));
            nextWorkoutDtoUsingLastWorkoutAsReference.setBlueprint(false);
            if (nextWorkoutDtoUsingLastWorkoutAsReference.getId() != null) {
                nextWorkoutDtoList.add(nextWorkoutDtoUsingLastWorkoutAsReference);
            }
        }
        return nextWorkoutDtoList;
    }

    private void sortByDayAndHour(List<ScheduleEntity> scheduleEntityList){
        scheduleEntityList.sort(Comparator
                .comparingInt(ScheduleEntity::getDayOfTheWeek)
                .thenComparing(ScheduleEntity::getHourStart));
    }
}
