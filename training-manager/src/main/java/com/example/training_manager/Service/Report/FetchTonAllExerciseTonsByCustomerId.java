package com.example.training_manager.Service.Report;

import com.example.training_manager.Dto.Report.TonReportGetDto;
import com.example.training_manager.Model.SetEntity;
import com.example.training_manager.Repository.SetRepository;
import com.example.training_manager.Service.Shared.ReturnTrainerIdFromJWT;
import com.example.training_manager.Service.Shared.ValidateTrainerOwnershipOverCustomer;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FetchTonAllExerciseTonsByCustomerId {

    private final ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer;
    private final SetRepository setRepository;

    FetchTonAllExerciseTonsByCustomerId(ValidateTrainerOwnershipOverCustomer validateTrainerOwnershipOverCustomer,
                                        SetRepository setRepository){
        this.validateTrainerOwnershipOverCustomer = validateTrainerOwnershipOverCustomer;
        this.setRepository = setRepository;
    }
    public float execute(TonReportGetDto tonReportGetDto, String authHeader) throws Exception{
        if(!validateTrainerOwnershipOverCustomer.execute(ReturnTrainerIdFromJWT.execute(authHeader), tonReportGetDto.getCustomerId())){
            throw new Exception("O treinador não possui permissão para este cliente.");
        }
        List<SetEntity> setEntityList = setRepository.returnAllSetFromExerciseBasedOnCustomerId(tonReportGetDto.getCustomerId(), tonReportGetDto.getExerciseId());
        float ton = 0;
        for (int i = 0; i<setEntityList.size(); i++){
            ton += setEntityList.get(i).getWeight() * setEntityList.get(i).getRepetitions();
        }
        return ton;
    }
}
