package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Dto.Schedule.ScheduleGetDto;
import com.example.training_manager.Model.WorkoutEntity;
import com.example.training_manager.Repository.CustomerRepository;
import com.example.training_manager.Repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/*implementacao lixo, regras de negocio sobre qual o parametro para escolher o treino atual nao bem definidos:
devo escolher o proximo treino em sequencia na agenda mesmo que ele esteja atrasado ou pular e pegar o proximo treino
da lista que bate com o dia de hoje? Se o primeiro entao a agenda é praticamente inutil e só deve guardar qual cliente
será atendido e outra entidade deve guardar a ordem de treinos sendo ignorante a qualquer outra informacao. É possivel
fazer isso usando o ProgramBlueprintEntity mas o prazo de entrega do trabalho acabou.
*/


/*vou implementar a ideia de seguir com a ordem de treinos dando a opcao de pular, mas vou manter a agenda, me
parece a forma mais inteligente de implementar considerando uma situação real*/

/*também acho que vou trocar date por calendar na V2.0 desse app, já que date é legado*/
@Service
public class WorkoutCustomerProfile {
    FetchScheduleByTrainer fetchScheduleByTrainer;
    CustomerRepository customerRepository;
    WorkoutRepository workoutRepository;

    @Autowired
    WorkoutCustomerProfile(FetchScheduleByTrainer fetchScheduleByTrainer,
                           CustomerRepository customerRepository,
                            WorkoutRepository workoutRepository){
        this.fetchScheduleByTrainer = fetchScheduleByTrainer;
        this.customerRepository = customerRepository;
        this.workoutRepository = workoutRepository;
    }

    public List<String> execute(String authHeader, Long id) throws Exception{
        List<String> workoutNames = new ArrayList<>();
        List<ScheduleGetDto> scheduleGetDtoList = fetchScheduleByTrainer.execute(authHeader);
        String customerName = customerRepository.findCustomerNameById(id);
        List<ScheduleGetDto> scheduleGetDtoListFilteredByCustomer = scheduleGetDtoList.stream()
                .filter(schedule -> customerName.equals(schedule.getCustomerName()))
                .toList();
        workoutNames.add(getNextWorkoutBasedInTheDayOfTheWeek(scheduleGetDtoListFilteredByCustomer));
        getLastThreeWorkoutsIfTheyExist(id, workoutNames);
        return workoutNames;
    }

    private String getNextWorkoutBasedInTheDayOfTheWeek(List<ScheduleGetDto> scheduleGetDtoListFilteredByCustomer){
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        for (ScheduleGetDto schedule : scheduleGetDtoListFilteredByCustomer) {
            DayOfWeek workoutDay = schedule.getDateStart().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .getDayOfWeek();
            if (workoutDay.equals(today)) {
                return schedule.getWorkoutName();
            }
        }
        return "NULO"; //cliente nao treina hoje
    }

    private void getLastThreeWorkoutsIfTheyExist(Long id, List<String> workoutNames) {
        List<WorkoutEntity> workoutEntityList = workoutRepository.returnWorkoutsDescendant(id);
        if (workoutEntityList == null || workoutEntityList.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                workoutNames.add("NULO");
            }
            return;
        }
        int count = 0;
        for (WorkoutEntity workout : workoutEntityList) {
            if (count >= 3) break;
            if (workout.getProgramEntity() == null || workout.getProgramEntity().isBlueprint()) {
                workoutNames.add("NULO");
            } else {
                workoutNames.add(workout.getName());
            }
            count++;
        }

        while (count < 3) {
            workoutNames.add("NULO");
            count++;
        }
    }


}
