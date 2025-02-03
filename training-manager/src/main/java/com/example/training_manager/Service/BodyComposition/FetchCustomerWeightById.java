package com.example.training_manager.Service.BodyComposition;

import com.example.training_manager.Dto.Weight.WeightDto;
import com.example.training_manager.Model.WeightEntity;
import com.example.training_manager.Repository.WeightRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateToken;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FetchCustomerWeightById {
    private final ValidateToken validateToken;
    private final WeightRepository weightRepository;


    @Autowired
    FetchCustomerWeightById(WeightRepository weightRepository,
                            ValidateToken validateToken){
        this.weightRepository = weightRepository;
        this.validateToken = validateToken;
    }

    public List<WeightDto> execute(long id, String authHeader){
        validateToken.execute(id, authHeader);
        List<WeightEntity> weightEntityList = weightRepository.findWeightEntitiesByCustomerId(id);
        List<WeightDto> weightDtoList = new ArrayList<>();
        return transformEntitiesIntoDtos(weightDtoList, weightEntityList);
    }

    private List<WeightDto> transformEntitiesIntoDtos(List<WeightDto> weightDtoList, List<WeightEntity> weightEntityList){
        for (int i = 0; i<weightEntityList.size(); i++){
            WeightDto weightDto = new WeightDto();
            weightDto.setWeight(weightEntityList.get(i).getWeight());
            weightDto.setCustomerId(weightEntityList.get(i).getCustomerEntity().getId());
            weightDto.setHeight(weightEntityList.get(i).getHeight());
            weightDto.setBodyFatPercentage(weightEntityList.get(i).getBodyFatPercentage());
            weightDto.setDate(weightEntityList.get(i).getDate());
            weightDto.setId(weightEntityList.get(i).getId());
            weightDtoList.add(weightDto);
        }
        weightDtoList.sort(Comparator.comparing(WeightDto::getDate));
        return weightDtoList;
    }
}
