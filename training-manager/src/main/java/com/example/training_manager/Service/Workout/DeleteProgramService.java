package com.example.training_manager.Service.Workout;
import com.example.training_manager.Repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteProgramService {
    private final ProgramRepository programRepository;

    @Autowired
    DeleteProgramService(ProgramRepository programRepository){
        this.programRepository = programRepository;
    }

    public void execute(){

    }
}
