package br.com.mybar.project.services;

import br.com.mybar.project.DAOs.ConsumptionInterface;
import br.com.mybar.project.DAOs.PayInterface;
import br.com.mybar.project.request.PayRequest.PayRevenueRequest;
import br.com.mybar.project.models.Consumption;
import br.com.mybar.project.models.Pay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class ConsumptionIntervalCalculationService {
        @Autowired
        private ConsumptionInterface DAO;

        public ArrayList<Consumption> getConsumptionInterval(String startDate, String endDate)
        {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Formatacao da data

            LocalDate start = LocalDate.parse(startDate, format);
            LocalDate end = LocalDate.parse(endDate, format);

            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(23, 59, 59);


            return DAO.findAllByDateBetween(startDateTime, endDateTime);
        }
}
