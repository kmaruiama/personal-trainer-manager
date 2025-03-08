package com.example.training_manager.Service.Schedule;

import com.example.training_manager.Dto.Schedule.NextWorkoutDto;
import com.example.training_manager.Dto.Schedule.NextWorkoutFilteredDto;
import com.example.training_manager.Service.Shared.ValidateToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReturnNextWorkoutFilteredService {
    private final ValidateToken validateToken;
    ReturnNextWorkoutUnfilteredService returnNextWorkoutUnfilteredService;

    @Autowired
    ReturnNextWorkoutFilteredService(ReturnNextWorkoutUnfilteredService returnNextWorkoutUnfilteredService, ValidateToken validateToken){
        this.returnNextWorkoutUnfilteredService = returnNextWorkoutUnfilteredService;
        this.validateToken = validateToken;
    }

    public List<NextWorkoutFilteredDto> execute(Long customerId, String authHeader){
        validateToken.execute(customerId, authHeader);
        List<NextWorkoutDto> unfilteredNextWorkoutDtoList = returnNextWorkoutUnfilteredService.execute(customerId, authHeader);
        return filter(unfilteredNextWorkoutDtoList);
    }

    private List<NextWorkoutFilteredDto> filter(List<NextWorkoutDto> unfilteredNextWorkoutDtoList){
        List<String> names = new ArrayList<>();
        //clear all repetitions
        for (int i = 0; i<unfilteredNextWorkoutDtoList.size(); i++){
           if (!names.contains(unfilteredNextWorkoutDtoList.get(i).getName())){
               names.add(unfilteredNextWorkoutDtoList.get(i).getName());
           }
        }
        //for each name, I'll create  a new filtered object
        List<NextWorkoutFilteredDto> filteredNextWorkoutDtoList = new ArrayList<>();
        for (int i = 0; i< names.size(); i++){
            NextWorkoutFilteredDto nextWorkoutFilteredDto = new NextWorkoutFilteredDto();
            nextWorkoutFilteredDto.setName(names.get(i));
            filteredNextWorkoutDtoList.add(nextWorkoutFilteredDto);
        }
        System.out.print(unfilteredNextWorkoutDtoList);
        //now, for each filtered workout, I'll set both blueprint and non-blueprint ids
        for (int i = 0; i<filteredNextWorkoutDtoList.size(); i++){
            for (int j = 0; j<unfilteredNextWorkoutDtoList.size(); j++){
                if (unfilteredNextWorkoutDtoList.get(j).getName().equals(filteredNextWorkoutDtoList.get(i).getName())
                        && unfilteredNextWorkoutDtoList.get(j).isBlueprint()){
                    filteredNextWorkoutDtoList.get(i).setBlueprintId(unfilteredNextWorkoutDtoList.get(j).getId());
                }
                if (unfilteredNextWorkoutDtoList.get(j).getName().equals(filteredNextWorkoutDtoList.get(i).getName())
                        && !unfilteredNextWorkoutDtoList.get(j).isBlueprint()){
                    filteredNextWorkoutDtoList.get(i).setNoBlueprintId(unfilteredNextWorkoutDtoList.get(j).getId());
                }

            }
        }
        //this is O(n^2), not good :(
        return filteredNextWorkoutDtoList;
    }
}

