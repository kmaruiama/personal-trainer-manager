package com.example.training_manager.Service.Workout;

import com.example.training_manager.Dto.Workout.ProgramDto;
import com.example.training_manager.Dto.Workout.WorkoutDto;
import com.example.training_manager.Model.ProgramEntity;
import com.example.training_manager.Repository.ProgramRepository;
import com.example.training_manager.Service.Shared.ValidateToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GetProgramService {

    private final ProgramRepository programRepository;
    private final ValidateToken validateToken;
    private final GetWorkoutService getWorkoutService;

    @Autowired
    GetProgramService (ProgramRepository programRepository,
                       ValidateToken validateToken,
                        GetWorkoutService getWorkoutService){
        this.programRepository = programRepository;
        this.validateToken = validateToken;
        this.getWorkoutService = getWorkoutService;
    }

    public ProgramDto execute (Long customerId, String authHeader) throws Exception {
        validateToken.execute(customerId, authHeader);
        ProgramEntity programEntity;
        Optional<ProgramEntity> programEntityOptional = programRepository.ReturnProgramFromCustomerId(customerId);
        if (programEntityOptional.isPresent()){
            programEntity = programEntityOptional.get();
        }
        else {
            throw new Exception("Erro ao encontrar programa de treinos");
        }

        ProgramDto programDto = new ProgramDto();
        programDto.setName(programEntity.getName());
        programDto.setId(programEntity.getId());
        List<WorkoutDto> workoutDtoList = new ArrayList<>();
        for (int i = 0; i<programEntity.getWorkouts().size(); i++){
            workoutDtoList.add(
                    getWorkoutService.transformWorkoutEntityIntoWorkoutDto(
                            programEntity.getWorkouts().get(i)));
        }
        programDto.setWorkoutDtoList(workoutDtoList);

        return programDto;
    }
}
