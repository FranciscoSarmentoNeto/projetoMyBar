package br.com.mybar.project.controllers;

import br.com.mybar.project.DAOs.ConsumptionInterface;
import br.com.mybar.project.DAOs.AccountInterface;
import br.com.mybar.project.DAOs.ItemInterface;
import br.com.mybar.project.request.ConsumptionRequest.ConsumptionIntervalReport;
import br.com.mybar.project.request.ConsumptionRequest.ConsumptionIntervalRequest;
import br.com.mybar.project.request.ConsumptionRequest.ConsumptionPostRequest;
import br.com.mybar.project.request.ConsumptionRequest.ConsumptionPutRequest;
import br.com.mybar.project.request.PayRequest.PayRevenueRequest;
import br.com.mybar.project.services.ConsumptionIntervalCalculationService;
import br.com.mybar.project.services.PayService.PayIntervalCalculationService;
import br.com.mybar.project.models.Consumption;
import br.com.mybar.project.models.Account;
import br.com.mybar.project.models.Item;
import br.com.mybar.project.models.Pay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class ConsumptionController {

    @Autowired
    private ConsumptionInterface consumptionDAO; // Automatically generates DAOs methods
    @Autowired
    private AccountInterface accountDAO;
    @Autowired
    private ItemInterface itemDAO;
    @Autowired
    private PayIntervalCalculationService payIntervalCalculationService;
    @Autowired
    private ConsumptionIntervalCalculationService consumptionIntervalCalculationService;

    @GetMapping("/consumptions")
    public ArrayList<Consumption> getConsumptions() // Recover all database consumptions
    {
        // Return List of consumptions or Empty List
        return (ArrayList<Consumption>) consumptionDAO.findAll();
    }

    @GetMapping("/consumption/{id}") // Search consumption by id
    public Optional<Consumption> searchConsumption(@PathVariable int id)
    {
        // Return consumption or Null
        return consumptionDAO.findById(id);
    }

    @GetMapping("/consumption/account/{id}") // Search consumption of an account
    public ArrayList<Consumption> searchConsumptionByAccount(@PathVariable int id)
    {
        // Return List of consumptions or Empty List
        return consumptionDAO.findByAccountId(id);
    }

    @GetMapping("consumption/account/client/{name}") // Search consumptions of client by name
    public ArrayList<Consumption> searchConsumptionsByAccountClientName(@PathVariable String name)
    {
        // Return List of consumptions or Empty List
        return consumptionDAO.findByAccountClientName(name);
    }

    @PostMapping("/consumption") // Save one new consumption
    public ResponseEntity<?> postConsumption(@RequestBody ConsumptionPostRequest consumptionRequest)
    {
        try {
            // Check if the account exists
            Account account = accountDAO.findById(consumptionRequest.getConta_id()).orElse(null);
            // Check if the item exists
            Item item = itemDAO.findById(consumptionRequest.getNum_item()).orElse(null);

            // Account not exists
            if (account == null)
                return ResponseEntity.status(404).body("Conta não existe!");
            // Item not exists
            if (item == null)
                return ResponseEntity.status(404).body("Item não existe!");

            if(!account.isOpen())
                return ResponseEntity.status(404).body("Conta fechada!");

            if(!item.isAvailable())
                return ResponseEntity.status(404).body("O pedido não está disponivel!");

            // Constructs consumption
            Consumption consumption = new Consumption();
            consumption.setId(0); // id is auto generated
            consumption.setAccount(account);
            consumption.setItem(item);
            consumption.setQuantity(consumptionRequest.getQuantidade());

            // return new consumption and server response
            return ResponseEntity.status(200).body(consumptionDAO.save(consumption));
        }catch (Exception e)
        {
            return ResponseEntity.status(500).body("Erro: " + e);
        }
    }

    @PutMapping("/consumption")
    public ResponseEntity<?> updateConsumption(@RequestBody ConsumptionPutRequest request) {

        // Buscar consumo
        Consumption consumption = consumptionDAO.findById(request.getConsumption_id()).orElse(null);

        if (consumption == null) {
            return ResponseEntity.status(404).body("Consumo não encontrado");
        }

        // Atualizar conta (se enviada)
        if (request.getConta_id() != null) {
            Account account = accountDAO.findById(request.getConta_id()).orElse(null);

            if (account == null) {
                return ResponseEntity.status(404).body("Conta não existe!");
            }

            consumption.setAccount(account);
        }

        // Atualizar item (se enviado)
        if (request.getNum_item() != null) {
            Item item = itemDAO.findById(request.getNum_item()).orElse(null);

            if (item == null) {
                return ResponseEntity.status(404).body("Item não existe!");
            }

            consumption.setItem(item);
        }

        // Atualizar quantidade (se enviada)
        if (request.getQuantidade() != null) {
            consumption.setQuantity(request.getQuantidade());
        }

        // Salvar mudanças diretamente
        consumptionDAO.save(consumption);

        return ResponseEntity.ok(consumption);
    }

    @DeleteMapping("/consumptions") // Delete all consumptions
    public void deleteAllConsumptions(){
        consumptionDAO.deleteAll();
    }

    @DeleteMapping("/consumptions/{id}") // Delete consumption by id
    public ResponseEntity<?> deleteConsumption(@PathVariable int id)
    {
        try{
            Consumption consumption = consumptionDAO.findById(id).orElse(null);

            if(consumption == null)
                return ResponseEntity.status(404).body("Consumo não existe!");

            consumptionDAO.deleteById(id);
            return ResponseEntity.ok("Item deletado");
        }catch (Exception e)
        {
            return ResponseEntity.status(500).body("Erro: " + e);
        }
    }

    @GetMapping("/consumption/interval")
    public ResponseEntity<?> getBarConsumptionInterval(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        try {

            // Formatos possiveis
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            LocalDate start;
            LocalDate end;

            start = LocalDate.parse(startDate, format);
            end = LocalDate.parse(endDate, format);


            // Se a data inicial for maior que a final, inverte
            if (start.isAfter(end)) {
                LocalDate temp = start;
                start = end;
                end = temp;
            }

            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(23, 59, 59);

            ArrayList<Consumption> consumptions = consumptionIntervalCalculationService.getConsumptionInterval(
                    startDateTime.format(format),
                    endDateTime.format(format)
            );
            ArrayList<ConsumptionIntervalRequest> request = new ArrayList<>();

            PayRevenueRequest payRequest = payIntervalCalculationService.intervalPayCalculation(
                    startDateTime.format(format),
                    endDateTime.format(format)
            );

            // Montar resposta
            for (Consumption c : consumptions) {
                ConsumptionIntervalRequest r = new ConsumptionIntervalRequest();

                r.setDate(c.getDate().format(format));
                r.setQuantity(c.getQuantity());
                r.setNameItem(c.getItem().getName());
                r.setClient_cpf(c.getAccount().getClient().getCpf());

                request.add(r);
            }

            ConsumptionIntervalReport report = new ConsumptionIntervalReport();
            report.setConsumptions(request);
            report.setRevenue(payRequest.getRevenue());

            return ResponseEntity.ok().body(report);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
    }

}
