package com.example.training_manager.Controller;

import com.example.training_manager.Dto.Workout.WorkoutDto;
import com.example.training_manager.Service.Workout.AddWorkoutService;
import com.example.training_manager.Service.Workout.GetWorkoutService;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/workout")
@CrossOrigin(origins = "http://localhost:8100")
public class WorkoutController {

    private final AddWorkoutService addWorkoutService;
    private final GetWorkoutService getWorkoutService;
    @Autowired
    WorkoutController (AddWorkoutService addWorkoutService,
                       GetWorkoutService getWorkoutService){
        this.addWorkoutService = addWorkoutService;
        this.getWorkoutService = getWorkoutService;
    }

    @PostMapping
    ResponseEntity<Map<String, String>> addWorkout(@RequestBody WorkoutDto workoutDto,
                                                  @RequestHeader("Authorization") String authHeader){
        try {
            addWorkoutService.execute(workoutDto, authHeader);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", "Novo treino adicionado com sucesso"));
        } catch (Exception e){
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
}
