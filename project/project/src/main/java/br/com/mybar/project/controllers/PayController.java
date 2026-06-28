package br.com.mybar.project.controllers;

import br.com.mybar.project.DAOs.AccountInterface;
import br.com.mybar.project.DAOs.PayInterface;
import br.com.mybar.project.request.PayRequest.PayPostRequest;
import br.com.mybar.project.request.PayRequest.PayPutRequest;
import br.com.mybar.project.request.PayRequest.PayRevenueRequest;
import br.com.mybar.project.services.PayService.PayIntervalCalculationService;
import br.com.mybar.project.services.PayService.PaymentService;
import br.com.mybar.project.models.Account;
import br.com.mybar.project.models.Pay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class PayController {

    @Autowired // Automatically generates DAOs methods
    private PayInterface payDAO;
    @Autowired
    private AccountInterface accountDAO;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PayIntervalCalculationService payIntervalCalculationService;

    @GetMapping("/payments") // Recover all database payments
    public ArrayList<Pay> getPayments()
    {
        // Return List of payments or Empty List
        return (ArrayList<Pay>) payDAO.findAll();
    }

    @GetMapping("/pay/id/{id}") // Search pay by id
    public Optional<Pay> getPay(@PathVariable int id)
    {
        // return pay or null
        return payDAO.findById(id);
    }

    @GetMapping("/pay/account/{id}") // Search payments by account id
    public ArrayList<Pay> getPaymentsByAccount(@PathVariable int id)
    {
        // Return List of payments or Empty List
        return payDAO.findByAccountId(id);
    }

    @GetMapping("/pay/{author}") // Search payments by author
    public ArrayList<Pay> getPaymentsByAuthor(@PathVariable String author)
    {
        // Return List of payments or Empty List
        return payDAO.findByAuthor(author);
    }

    @PostMapping("/pay") // Save one new pay on database
    public ResponseEntity<?> postPay(@RequestBody PayPostRequest payRequest) throws IOException {

        try {
            if (!paymentService.validatedPayment(payRequest.getConta_id(), payRequest.getValor()))
                return ResponseEntity.status(404).body("Conta já foi pago");

            // Check if account exists
            Account account = accountDAO.findById(payRequest.getConta_id()).orElse(null);

            // Account not exists in database
            if (account == null)
                return ResponseEntity.status(404).body("Conta não encontrada");

            if(account.isOpen())
                return ResponseEntity.status(404).body("Conta não está fechada!");

            // Construct pay
            Pay pay = new Pay();
            pay.setId(0); // id is auto generated
            pay.setAccount(account);
            pay.setAuthor(payRequest.getAutor());
            pay.setValue(payRequest.getValor());

            // Save pay and return server response
            return ResponseEntity.ok().body(payDAO.save(pay));
        }catch (Exception e)
        {
            return ResponseEntity.status(500).body("Erro: " + e);
        }
    }

    @PutMapping("/pay")
    public ResponseEntity<?> updatePay(@RequestBody PayPutRequest request) throws IOException {
        Pay pay = payDAO.findById(request.getPay_id()).orElse(null);

        if (pay == null)
            return ResponseEntity.status(404).body("Não foi possivel atualizar o pagamento, pois ele não existe!");

        if(request.getConta_id() != null) {
            // Check if account exists
            Account account = accountDAO.findById(request.getConta_id()).orElse(null);

            // Account not exists in database
            if (account == null)
                return ResponseEntity.status(404).body("Conta não encontrada");

            pay.setAccount(account);
        }

        if(request.getAutor() != null)
            pay.setAuthor(request.getAutor());

        if(request.getValor() != null) {
            if (request.getValor() > pay.getValue()) {
                if (paymentService.validatedPayment(
                        request.getConta_id() == null ? pay.getAccount().getAccountId() : request.getConta_id(),
                        request.getValor() - pay.getValue()
                    )
                )
                    pay.setValue(request.getValor());
            }
            else
                pay.setValue(request.getValor());
        }

        // Save pay and return server response
        return ResponseEntity.ok().body(payDAO.save(pay));
    }

    @DeleteMapping("/payments") // Delete all payments in database
    public void deleteAllPayments()
    {
        payDAO.deleteAll();
    }

    @DeleteMapping("/pay/{id}") // Delete pay in database by id
    public void deletePay(@PathVariable int id)
    {
        payDAO.deleteById(id);
    }

    @GetMapping("/pay/interval")
    public ResponseEntity<?> getBarRecipe(@RequestParam String startDate, @RequestParam String endDate)
    {
        try {
            PayRevenueRequest request = payIntervalCalculationService.intervalPayCalculation(startDate, endDate);

            return ResponseEntity.ok().body(request);
        }catch (Exception e)
        {
            return ResponseEntity.status(500).body("Erro: " + e);
        }
    }
}
