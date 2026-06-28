package br.com.mybar.project.services.PayService;

import br.com.mybar.project.DAOs.PayInterface;
import br.com.mybar.project.request.PayRequest.PayRevenueRequest;
import br.com.mybar.project.models.Pay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class PayIntervalCalculationService {

    @Autowired
    private PayInterface payDAO;

    public PayRevenueRequest intervalPayCalculation(String startDate, String endDate)
    {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Formatacao da data

        LocalDate start = LocalDate.parse(startDate, format);
        LocalDate end = LocalDate.parse(endDate, format);

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);

        ArrayList<Pay> payments = payDAO.findAllByDateBetween(startDateTime, endDateTime);

        PayRevenueRequest request = new PayRevenueRequest();

        for(Pay pay: payments)
            request.revenueIncrement(pay.getValue());

        request.setAmountPayments(payments.size());

        return request;
    }
}
