package com.example.training_manager.Controller;

import com.example.training_manager.Dto.Schedule.NextWorkoutDto;
import com.example.training_manager.Dto.Schedule.NextWorkoutFilteredDto;
import com.example.training_manager.Dto.Workout.DeleteWorkoutDto;
import com.example.training_manager.Dto.Workout.ProgramDto;
import com.example.training_manager.Dto.Workout.WorkoutDto;
import com.example.training_manager.Service.Schedule.ReturnNextWorkoutFilteredService;
import com.example.training_manager.Service.Schedule.ReturnNextWorkoutUnfilteredService;
import com.example.training_manager.Service.Workout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/workout")
@CrossOrigin(origins = "http://localhost:8100")
public class WorkoutController {

    private final AddWorkoutService addWorkoutService;
    private final GetWorkoutService getWorkoutService;
    private final EditWorkoutService editWorkoutService;
    private final GetProgramService getProgramService;
    private final AddProgramService addProgramService;
    private final DeleteWorkoutService deleteWorkoutService;
    private final ReturnNextWorkoutFilteredService returnNextWorkoutFilteredService;

    @Autowired
    WorkoutController (AddWorkoutService addWorkoutService,
                       GetWorkoutService getWorkoutService,
                       EditWorkoutService editWorkoutService,
                       GetProgramService getProgramService,
                       AddProgramService addProgramService,
                       DeleteWorkoutService deleteWorkoutService,
                       ReturnNextWorkoutFilteredService returnNextWorkoutFilteredService){
        this.addWorkoutService = addWorkoutService;
        this.getWorkoutService = getWorkoutService;
        this.editWorkoutService = editWorkoutService;
        this.getProgramService = getProgramService;
        this.addProgramService = addProgramService;
        this.deleteWorkoutService = deleteWorkoutService;
        this.returnNextWorkoutFilteredService = returnNextWorkoutFilteredService;
    }


    @PostMapping
    @RequestMapping("/program")
    ResponseEntity<Map<String, String>> addProgram(@RequestBody ProgramDto programDto,
                                                   @RequestHeader("Authorization") String authHeader){
        try {
            addProgramService.execute(programDto, authHeader);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", "Novo programa adicionado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao adicionar novo programa de treinos"));
        }
    }

    @PostMapping
    ResponseEntity<Map<String, String>> addWorkout(@RequestBody WorkoutDto workoutDto,
                                                  @RequestHeader("Authorization") String authHeader){
        try {
            addWorkoutService.execute(workoutDto, authHeader);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", "Novo treino adicionado com sucesso"));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao adicionar novo treino"));
        }
    }

    @GetMapping
    ResponseEntity<WorkoutDto> getWorkout(@RequestParam Long id,
                                          @RequestHeader("Authorization") String authHeader){
        try {
            return ResponseEntity.ok(getWorkoutService.execute(id, authHeader));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/program")
    ResponseEntity<ProgramDto> returnProgram(@RequestParam Long id,
                                                 @RequestHeader ("Authorization") String authHeader){
        try {
            return ResponseEntity.ok(getProgramService.execute(id, authHeader));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping
    ResponseEntity<Map<String, String>> editWorkout(@RequestBody WorkoutDto workoutDto,
                                                   @RequestHeader("Authorization") String authHeader){
        try {
            editWorkoutService.execute(workoutDto, authHeader);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", "Treino editado com sucesso"));
        } catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao editar treino"));
        }
    }

    @DeleteMapping("/blueprint")
    ResponseEntity<Map<String, String>> deleteWorkout(@RequestBody DeleteWorkoutDto deleteWorkoutDto,
                                                      @RequestHeader("Authorization") String authHeader){
        try {
            deleteWorkoutService.execute(deleteWorkoutDto, authHeader);
            return switch (deleteWorkoutDto.getTreeDeletionLevel()) {
                case 1 -> ResponseEntity.ok(Map.of("message", "Treino deletado com sucesso"));
                case 2 -> ResponseEntity.ok(Map.of("message", "Exercicio deletado com sucesso"));
                case 3 -> ResponseEntity.ok(Map.of("message", "Serio deletado com sucesso"));
                default -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Árvore inválida"));
            };
        } catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao deletar"));
        }
    }

    @GetMapping("/next")
    ResponseEntity<List<NextWorkoutFilteredDto>> returnNextWorkout(@RequestParam Long customerId,
                                                           @RequestHeader("Authorization") String authHeader){
        List<NextWorkoutFilteredDto> nextWorkoutDtoList = returnNextWorkoutFilteredService.execute(customerId, authHeader);
        return ResponseEntity.ok(nextWorkoutDtoList);
    }
}
