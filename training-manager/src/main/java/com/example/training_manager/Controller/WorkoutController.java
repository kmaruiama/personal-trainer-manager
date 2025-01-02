package com.example.training_manager.Controller;

import com.example.training_manager.Dto.Workout.ProgramDto;
import com.example.training_manager.Dto.Workout.ProgramBlueprintGetDto;
import com.example.training_manager.Service.Workout.AddProgramService;
import com.example.training_manager.Service.Workout.EditWorkoutService;
import com.example.training_manager.Service.Workout.FetchProgramBlueprintByCustomerId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/workout")
@CrossOrigin(origins = "http://localhost:8100")
public class WorkoutController {
    private final AddProgramService addProgramService;
    private final EditWorkoutService editWorkoutService;
    private final FetchProgramBlueprintByCustomerId fetchProgramBlueprintByCustomerId;

    @Autowired
    public WorkoutController(AddProgramService addProgramService,
                             EditWorkoutService editWorkoutService,
                             FetchProgramBlueprintByCustomerId fetchProgramBlueprintByCustomerId) {
        this.addProgramService = addProgramService;
        this.editWorkoutService = editWorkoutService;
        this.fetchProgramBlueprintByCustomerId = fetchProgramBlueprintByCustomerId;
    }

    @PostMapping
    public ResponseEntity<String> createNewWorkoutBlueprint(@RequestBody ProgramDto programDto,
                                                            @RequestHeader("Authorization") String authHeader){
        try {
            addProgramService.execute(programDto, authHeader);
            return ResponseEntity.status(HttpStatus.OK).body("Programa de treinos adicionado com suceso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar novo treino: " + e.getMessage());
        }
    }

    @PatchMapping
    public ResponseEntity<String> editWorkoutBlueprint(@RequestBody ProgramBlueprintGetDto programBlueprintGetDto,
                                                       @RequestHeader("Authorization") String authHeader){
        try {
            editWorkoutService.execute(programBlueprintGetDto, authHeader);
            return ResponseEntity.status(HttpStatus.OK).body("Programa de treinos editado com sucesso");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao editar programa de treinos: "+ e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<ProgramBlueprintGetDto> getProgramBlueprint(@RequestParam Long id,
                                                                      @RequestHeader("Authorization") String authHeader){
        try{
            return ResponseEntity.ok(fetchProgramBlueprintByCustomerId.execute(id, authHeader));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
