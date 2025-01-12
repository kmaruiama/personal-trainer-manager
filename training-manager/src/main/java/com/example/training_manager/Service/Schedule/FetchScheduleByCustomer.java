package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Dto.Schedule.ScheduleGetDto;
import com.example.training_manager.Model.ScheduleEntity;
import com.example.training_manager.Repository.ScheduleRepository;
import com.example.training_manager.Service.Shared.ValidateToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FetchScheduleByCustomer {
    ValidateToken validateToken;
    ScheduleRepository scheduleRepository;

    @Autowired
    FetchScheduleByCustomer(ValidateToken validateToken,
                            ScheduleRepository scheduleRepository){
        this.validateToken = validateToken;
        this.scheduleRepository = scheduleRepository;
    }

    public List<ScheduleGetDto> execute(Long customerId, String authHeader) throws Exception{
        validateToken.execute(customerId, authHeader);
        List<ScheduleEntity> scheduleEntityList = scheduleRepository.findScheduleEntitiesByCustomerEntityId(customerId);
        if (scheduleEntityList.isEmpty()){
            throw new Exception("Não há horários para esse cliente");
        }
        return convertToDto(scheduleEntityList);
    }

    private List<ScheduleGetDto> convertToDto(List <ScheduleEntity> scheduleEntityList){
        List <ScheduleGetDto> scheduleGetDtoList = new ArrayList<>();
        for (int i = 0; i<scheduleEntityList.size(); i++){
            ScheduleGetDto scheduleGetDto = new ScheduleGetDto();
            scheduleGetDto.setScheduleId(scheduleEntityList.get(i).getId());
            scheduleGetDto.setCustomerId(scheduleEntityList.get(i).getCustomerEntity().getId());
            scheduleGetDto.setWorkoutName(scheduleEntityList.get(i).getWorkoutEntity().getName());
            scheduleGetDto.setCustomerName(scheduleEntityList.get(i).getCustomerEntity().getNome());
            scheduleGetDto.setDateStart(scheduleEntityList.get(i).getDateStart());
            scheduleGetDto.setDateEnd(scheduleEntityList.get(i).getDateEnd());
            scheduleGetDtoList.add(scheduleGetDto);
        }
        return scheduleGetDtoList;
    }
}
