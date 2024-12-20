package com.example.training_manager.Controller;

import com.example.training_manager.Dto.Payment.PaymentDto;
import com.example.training_manager.Dto.Payment.PaymentGetDto;
import com.example.training_manager.Service.Payment.AddPaymentService;
import com.example.training_manager.Service.Payment.FetchPaymentByCustomer;
import com.example.training_manager.Service.Payment.FetchPaymentsByTrainer;
import com.example.training_manager.Service.Payment.PaymentEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment/")
public class PaymentController {
    private final AddPaymentService addPaymentService;
    private final FetchPaymentByCustomer fetchPaymentByCustomer;
    private final FetchPaymentsByTrainer fetchPaymentByTrainer;
    private final PaymentEditService paymentEditService;

    @Autowired
    PaymentController(AddPaymentService addPaymentService,
                      FetchPaymentByCustomer fetchPaymentByCustomer,
                      FetchPaymentsByTrainer fetchPaymentByTrainer,
                      PaymentEditService paymentEditService){
        this.addPaymentService = addPaymentService;
        this.fetchPaymentByCustomer = fetchPaymentByCustomer;
        this.fetchPaymentByTrainer = fetchPaymentByTrainer;
        this.paymentEditService = paymentEditService;
    }

    @PostMapping("/new")
    public ResponseEntity<String> addNewPayment(@RequestBody PaymentDto paymentDto,
                                                @RequestHeader("Authorization")String authHeader){
        try{
            addPaymentService.execute(authHeader, paymentDto);
            return ResponseEntity.status(HttpStatus.OK).body("Pagamento do cliente realizado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao inserir novo pagamento: " + e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<PaymentGetDto> showCustomerPayment (@RequestParam long id,
                                                              @RequestHeader("Authorization") String authHeader)
    {
        try{
            PaymentGetDto paymentGetDto = fetchPaymentByCustomer.execute(authHeader, id);
            return ResponseEntity.ok(paymentGetDto);
        }
        catch(Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<PaymentGetDto>> listAllTrainersPayments (@RequestHeader("Authorization") String authHeader){
        try {
            List<PaymentGetDto> list = fetchPaymentByTrainer.execute(authHeader);
            return ResponseEntity.ok(list);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/edit")
    public ResponseEntity<String> editPayment(@RequestBody PaymentDto paymentDto,
                                              @RequestHeader("Authorization") String authHeader){
        try{
            paymentEditService.execute(paymentDto, authHeader);
            return ResponseEntity.status(HttpStatus.OK).body("pagamento editado com sucesso!");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("erro ao editar informações de pagamento: "+ e);
        }
    }
}
