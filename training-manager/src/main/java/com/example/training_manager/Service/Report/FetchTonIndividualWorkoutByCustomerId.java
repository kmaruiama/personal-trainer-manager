package com.example.training_manager.Service.Report;


import com.example.training_manager.Dto.Report.TonReportGetDto;
import com.example.training_manager.Model.SetEntity;
import com.example.training_manager.Repository.SetRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FetchTonIndividualWorkoutByCustomerId {
    private final SetRepository setRepository;
    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;

    @Autowired
    FetchTonIndividualWorkoutByCustomerId(SetRepository setRepository,
                                          ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer){
        this.setRepository = setRepository;
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
    }

    public Float execute(TonReportGetDto tonReportGetDto, String authHeader) throws Exception{
        if(!validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader), tonReportGetDto.getCustomerId())){
            throw new Exception("O treinador não possui permissão para este cliente.");
        }
        float totalWeight = 0f;
        List<SetEntity> setEntityList = setRepository.returnAllSetFromWorkoutBasedOnWorkoutId(tonReportGetDto.getWorkoutId());
        for(int i = 0; i<setEntityList.size(); i++){
            totalWeight += setEntityList.get(i).getWeight()*setEntityList.get(i).getRepetitions();
        }
        return totalWeight;
    }
}
