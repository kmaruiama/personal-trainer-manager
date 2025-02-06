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

/*pq eu tinha escrito esse serviço de forma tao complicada sem motivo???*/
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

    public List<String> execute(String authHeader, Long id){
        //vai na pagina do cliente
        List<String> workoutNames = new ArrayList<>();
        //pega toda a agenda do treinador
        List<ScheduleGetDto> scheduleGetDtoList = fetchScheduleByTrainer.execute(authHeader);
        String customerName = customerRepository.findCustomerNameById(id);
        //filtra só os compromissos do cliente
        List<ScheduleGetDto> scheduleGetDtoListFilteredByCustomer = scheduleGetDtoList.stream()
                .filter(schedule -> customerName.equals(schedule.getCustomerName()))
                .toList();
        //joga no primeiro node da lista o treino de hoje, se houver
        workoutNames.add(getNextWorkoutBasedInTheDayOfTheWeek(scheduleGetDtoListFilteredByCustomer));
        //joga os últimos três treinos realizados, se aconteceram
        getLastThreeWorkoutsIfTheyExist(id, workoutNames);
        return workoutNames;
    }

    private String getNextWorkoutBasedInTheDayOfTheWeek(List<ScheduleGetDto> scheduleGetDtoListFilteredByCustomer){
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        for (ScheduleGetDto schedule : scheduleGetDtoListFilteredByCustomer) {
            if (schedule.getDayOfTheWeek() == today.getValue()) {
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

        for (int i = 0; i < 3; i++) {
            //se nao houveram 3 treinos ainda, adicionar nulo 
            if (i >= workoutEntityList.size()) {
                workoutNames.add("NULO");
            } else {
                if (workoutEntityList.get(i).getProgramEntity() == null || workoutEntityList.get(i).getProgramEntity().isBlueprint()) {
                    workoutNames.add("NULO");
                } else {
                    workoutNames.add(workoutEntityList.get(i).getName());
                }
            }
        }
    }


}
