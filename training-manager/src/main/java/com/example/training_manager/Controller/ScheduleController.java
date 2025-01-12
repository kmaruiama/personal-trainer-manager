package com.example.training_manager.Controller;


import com.example.training_manager.Dto.Schedule.ScheduleDto;
import com.example.training_manager.Dto.Schedule.ScheduleGetDto;
import com.example.training_manager.Service.Schedule.*;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule/")
@CrossOrigin(origins = "http://localhost:8100")
public class ScheduleController {
    private final AddScheduleService addScheduleService;
    private final FetchScheduleByTrainer fetchScheduleByTrainer;
    private final ScheduleEditService scheduleEditService;
    private final ScheduleDeleteService scheduleDeleteService;
    private final WorkoutCustomerProfile workoutCustomerProfile;
    private final FetchScheduleByCustomer fetchScheduleByCustomer;

    @Autowired
    public ScheduleController(AddScheduleService addScheduleService,
                              FetchScheduleByTrainer fetchScheduleByTrainer,
                              ScheduleEditService editScheduleService,
                              ScheduleDeleteService scheduleDeleteService,
                              WorkoutCustomerProfile workoutCustomerProfile,
                              FetchScheduleByCustomer fetchScheduleByCustomer){
        this.addScheduleService = addScheduleService;
        this.fetchScheduleByTrainer = fetchScheduleByTrainer;
        this.scheduleEditService = editScheduleService;
        this.scheduleDeleteService = scheduleDeleteService;
        this.workoutCustomerProfile = workoutCustomerProfile;
        this.fetchScheduleByCustomer = fetchScheduleByCustomer;
    }

    @GetMapping("/list")
    public ResponseEntity<List<ScheduleGetDto>> listAllTrainersSchedule (@RequestHeader("Authorization") String authHeader){
        try {
            List<ScheduleGetDto> list = fetchScheduleByTrainer.execute(authHeader);
            return ResponseEntity.ok(list);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<String> createNewSchedule(@RequestBody ScheduleDto scheduleDto,
                                                    @RequestHeader("Authorization") String authHeader)
    {
        try {
            addScheduleService.execute(authHeader, scheduleDto);
            return ResponseEntity.status(HttpStatus.OK).body("Horário salvo com sucesso");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar horário na agenda: " + e.getMessage());
        }
    }

    @PatchMapping("/edit")
    public ResponseEntity<String> editSchedule(@RequestBody ScheduleGetDto scheduleGetDto,
                                               @RequestHeader("Authorization") String authHeader){
        try{
            scheduleEditService.execute(authHeader, scheduleGetDto);
            return ResponseEntity.status(HttpStatus.OK).body("Horário editado com sucesso");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao editar horário na agenda: " + e);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<List<String>> getWorkoutListThatGoesInTheCustomerProfile(@RequestParam("id") Long id,
                                                                                   @RequestHeader("Authorization") String authHeader) {
        try {
            return ResponseEntity.ok(workoutCustomerProfile.execute(authHeader, id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteSchedule(@RequestParam Long id,
                                                 @RequestHeader("Authorization")String authHeader){
        try{
            scheduleDeleteService.execute(authHeader, id);
            return ResponseEntity.status(HttpStatus.OK).body("Horário deletado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir horário: " + e.getMessage());
        }
    }

    @GetMapping("/customer")
    public ResponseEntity<List<ScheduleGetDto>> getCompleteScheduleFromSingleCustomer(@RequestParam Long id,
                                                                                      @RequestHeader("Authorization")String authHeader)
    {
        try{
            return ResponseEntity.ok(fetchScheduleByCustomer.execute(id, authHeader));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

