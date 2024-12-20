package com.example.training_manager.Controller;


import com.example.training_manager.Dto.Weight.WeightDto;
import com.example.training_manager.Dto.Weight.WeightEditDto;
import com.example.training_manager.Service.BodyComposition.AddWeightService;
import com.example.training_manager.Service.BodyComposition.FetchCustomerWeightById;
import com.example.training_manager.Service.BodyComposition.WeightEditService;
import com.example.training_manager.Service.BodyComposition.WeightDeleteService;
import com.example.training_manager.Service.BodyComposition.RetrieveLastInput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/weight")
@CrossOrigin(origins = "http://localhost:8100")
public class WeightController {
    private final AddWeightService addWeightService;
    private final FetchCustomerWeightById fetchCustomerWeightById;
    private final WeightEditService weightEditService;
    private final WeightDeleteService weightDeleteService;
    private final RetrieveLastInput retrieveLastInput;

    WeightController(AddWeightService addWeightService,
                     FetchCustomerWeightById fetchCustomerWeightById,
                     WeightEditService weightEditService,
                     WeightDeleteService weightDeleteService,
                     RetrieveLastInput retrieveLastInput){
        this.addWeightService = addWeightService;
        this.fetchCustomerWeightById = fetchCustomerWeightById;
        this.weightEditService = weightEditService;
        this.weightDeleteService = weightDeleteService;
        this.retrieveLastInput = retrieveLastInput;
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> addNewWeight(@RequestBody WeightDto weightDto,
                                                            @RequestHeader("Authorization") String authHeader) {
        try {
            addWeightService.execute(weightDto, authHeader);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Informações corporais inseridas com sucesso."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao inserir informações corporais: " + e.getMessage()));
        }
    }


    @GetMapping("/list")
    public ResponseEntity<List<WeightDto>> retrieveWeightFromCustomer (@RequestParam Long customerId,
                                                       @RequestHeader("Authorization")String authHeader){
        try{
            System.out.println(fetchCustomerWeightById.execute(customerId, authHeader));
            return ResponseEntity.ok(fetchCustomerWeightById.execute(customerId, authHeader));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/last")
    public ResponseEntity<WeightDto> retrieveLastWeight(@RequestParam Long customerId,
                                        @RequestHeader("Authorization")String authHeader){
        try{
            return ResponseEntity.ok(retrieveLastInput.execute(customerId, authHeader));
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/edit")
    public ResponseEntity<String> editWeight(@RequestBody WeightEditDto weightEditDto,
                                             @RequestHeader("Authorization")String authHeader){
        try{
            weightEditService.execute(weightEditDto, authHeader);
            return ResponseEntity.status(HttpStatus.OK).body("Informações corporais editadas com sucesso.");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao editar informações corporais: " + e);
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteWeight(@RequestBody Long id,
                                               @RequestHeader("Authorization")String authHeader){
        try{
            weightDeleteService.execute(id, authHeader);
            return ResponseEntity.status(HttpStatus.OK).body("Informações corporais deletadas com sucesso.");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar informações corporais: " + e);
        }
    }
}
