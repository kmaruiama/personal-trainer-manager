package com.example.training_manager.Service.Payment;

import com.example.training_manager.Dto.Payment.PaymentDto;
import com.example.training_manager.Service.Shared.ValidateToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Service
public class AddFuturePaymentIfTheClientAlreadyPayed {

    private final ValidateToken validateToken;
    private final AddPaymentService addPaymentService;

    @Autowired
    AddFuturePaymentIfTheClientAlreadyPayed(ValidateToken validateToken,
                                            AddPaymentService addScheduleService){
        this.validateToken = validateToken;
        this.addPaymentService = addScheduleService;
    }

    public void execute(PaymentDto paymentDto, String authHeader) throws Exception{
        if (paymentDto.getModalidade().equals("Único")){
            return;
        }
        System.out.print(paymentDto.getCustomerId());
        validateToken.execute(paymentDto.getCustomerId(), authHeader);
        PaymentDto futurePaymentDto = new PaymentDto();
        futurePaymentDto.setDataVencimento(calculateFuturePaymentDate(paymentDto));
        futurePaymentDto.setPreco(paymentDto.getPreco());
        futurePaymentDto.setCustomerId(paymentDto.getCustomerId());
        futurePaymentDto.setPayed(false);
        futurePaymentDto.setModalidade(paymentDto.getModalidade());
        addPaymentService.execute(authHeader, futurePaymentDto);
    }

    private Date calculateFuturePaymentDate(PaymentDto paymentDto) throws Exception{
        LocalDate vencimento = paymentDto.getDataVencimento().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
        switch (paymentDto.getModalidade()) {
            case "Semanal":
                vencimento = vencimento.plus(7, ChronoUnit.DAYS);
                break;
            case "Mensal":
                vencimento = vencimento.plus(1, ChronoUnit.MONTHS);
                break;
            default:
                throw new Exception("Erro ao identificar modalidade de pagamento");
        }
        return Date.from(vencimento.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}