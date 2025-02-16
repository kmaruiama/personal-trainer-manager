package com.example.training_manager.Controller;


import com.example.training_manager.Dto.Customer.CustomerInfoDto;
import com.example.training_manager.Dto.Customer.CustomerListGetDto;
import com.example.training_manager.Dto.Customer.CustomerPricingRawDto;
import com.example.training_manager.Service.Customer.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer/")
@CrossOrigin(origins = "http://localhost:8100")
public class CustomerController {
    private final CustomerRegisterService customerRegisterService;
    private final FetchCustomersByTrainer fetchCustomersByTrainer;
    private final SearchCustomerDetails searchCustomerDetails;
    private final CustomerEditService customerEditService;
    private final CustomerDeleteService customerDeleteService;


    @Autowired
    public CustomerController(CustomerRegisterService customerRegisterService,
                              FetchCustomersByTrainer fetchCustomersByTrainer,
                              SearchCustomerDetails searchCustomerDetails,
                              CustomerEditService customerEditService,
                              CustomerDeleteService customerDeleteService) {
        this.customerRegisterService = customerRegisterService;
        this.fetchCustomersByTrainer = fetchCustomersByTrainer;
        this.searchCustomerDetails = searchCustomerDetails;
        this.customerEditService = customerEditService;
        this.customerDeleteService = customerDeleteService;
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> addNewCustomer(@Valid @RequestBody CustomerPricingRawDto customerPricingRawDto,
                                                              @RequestHeader("Authorization") String authHeader) {
            customerRegisterService.execute(customerPricingRawDto, authHeader);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Cliente criado com sucesso"));
    }


    @GetMapping("/list")
    public ResponseEntity<List<CustomerListGetDto>> listAllTrainersCustomers (@RequestHeader("Authorization") String authHeader){
        try {
        return ResponseEntity.ok(fetchCustomersByTrainer.execute(authHeader));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/profile")
        public ResponseEntity<CustomerInfoDto> showCustomerInfo(@RequestParam  Long id,
                                                                @RequestHeader("Authorization") String authHeader) {
        try{
            return ResponseEntity.ok(searchCustomerDetails.execute(authHeader, id));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PatchMapping("/profile/edit")
    public ResponseEntity<Map<String, String>> editCustomer(@RequestBody CustomerInfoDto customerInfoDto,
                                                            @RequestHeader("Authorization") String authHeader) {
        try {
            customerEditService.execute(authHeader, customerInfoDto);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Cliente editado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro durante a edição: " + e.getMessage()));
        }
    }

    @DeleteMapping("profile/delete")
    public ResponseEntity<Map<String, String>> deleteCustomer(@RequestParam Long id,
                                                              @RequestHeader("Authorization") String authHeader) {
        try {
            customerDeleteService.execute(authHeader, id);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Cliente deletado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao excluir cliente: " + e.getMessage()));
        }
    }
}


