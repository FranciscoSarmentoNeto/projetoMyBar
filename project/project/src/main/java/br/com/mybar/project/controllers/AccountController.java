package br.com.mybar.project.controllers;

import br.com.mybar.project.DAOs.ClientInterface;
import br.com.mybar.project.DAOs.AccountInterface;
import br.com.mybar.project.DAOs.ConsumptionInterface;
import br.com.mybar.project.DAOs.ItemInterface;
import br.com.mybar.project.request.AcccountRequest.AccountAllRequest;
import br.com.mybar.project.request.AcccountRequest.AccountPostRequest;
import br.com.mybar.project.request.AcccountRequest.AccountPutRequest;
import br.com.mybar.project.request.AcccountRequest.AccountOpenRequest;
import br.com.mybar.project.request.TipRequest.TipRequest;
import br.com.mybar.project.request.TipRequest.TipValuesRequest;
import br.com.mybar.project.services.AccountService.AccountCalculationConsumptionsService;
import br.com.mybar.project.services.AccountService.AccountCalculationValueService;
import br.com.mybar.project.services.AccountService.AccountOpenService;
import br.com.mybar.project.services.AccountService.AccountValidationService;
import br.com.mybar.project.services.TipService.TipCalculationService;
import br.com.mybar.project.services.TipService.TipManagerService;
import br.com.mybar.project.models.Client;
import br.com.mybar.project.models.Account;
import br.com.mybar.project.models.Consumption;
import br.com.mybar.project.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class AccountController {

    @Autowired
    private AccountInterface accountDAO;// Automatically generates DAOs methods
    @Autowired
    private ClientInterface clientDAO;
    @Autowired
    private AccountCalculationConsumptionsService accountCalculationConsumptionsService;
    @Autowired
    private TipCalculationService tipCalculationService;
    @Autowired
    private ConsumptionInterface consumptionDAO;
    @Autowired
    private AccountValidationService validatedAccountService;
    @Autowired
    private AccountCalculationValueService accountCalculationValueService;
    @Autowired
    private ItemInterface itemDAO;
    @Autowired
    private AccountOpenService openAccountService;
    @Autowired
    private TipManagerService tipManagerService;

    @GetMapping("/accounts")
    public ResponseEntity<ArrayList<AccountAllRequest>>getAccounts() // Recover all database accounts
    {
        // Return List of accounts or Empty List
        ArrayList<Account> accounts = (ArrayList<Account>) accountDAO.findAll();
        ArrayList<AccountAllRequest> request = new ArrayList<>();
        for(Account account : accounts) {
            AccountAllRequest tempRequest = new AccountAllRequest();
            tempRequest.setId(account.getId());
            tempRequest.setCpf(account.getClient().getCpf());
            tempRequest.setPessoas(account.getPeoples());
            tempRequest.setOpen(account.isOpen());

            tempRequest.setCouvert(consumptionDAO.existsCouvertByAccountId(account.getAccountId()));

            request.add(tempRequest);
        }
        return ResponseEntity.ok(request);
    }

    @GetMapping("/account/{id}")
    public Optional<Account> searchAccount(@PathVariable int id) // Search account by id
    {
        // Return Account or Null
        return accountDAO.findById(id);
    }

    @PostMapping("/account")
    public ResponseEntity<?> postAccount(@RequestBody AccountPostRequest accountRequest) // Save one new account
    {
        if(!validatedAccountService.validateOpenAccount(accountRequest.getCliente_cpf()))
            return ResponseEntity.status(500).body("O cliente já tem uma conta aberta!");

        // Check if client exists
        Optional<Client> client = clientDAO.findById(accountRequest.getCliente_cpf());

        // client not exists
        if(client.isEmpty())
            return ResponseEntity.status(404).body("Não foi possível achar o cliente");

        // get client
        Client newClient = client.get();

        // Construct account
        Account account = new Account();
        account.setAccountId(0); // id is auto generated
        account.setClient(newClient);
        account.setOpen(accountRequest.getOpen());
        account.setPeoples(accountRequest.getPeoples());

        // Save account and return serve response
        return ResponseEntity.ok().body(accountDAO.save(account));
    }

    @PutMapping("/account")
    public ResponseEntity<?> updateAccount(@RequestBody AccountPutRequest request)
    {
        Account account = accountDAO.findById(request.getAccount_id()).orElse(null);

        if (account == null) {
            return ResponseEntity.status(404).body("Conta não existe!");
        }

        // Atualizar cliente se enviado
        if (request.getCliente_cpf() != null) {
            Client client = clientDAO.findById(request.getCliente_cpf()).orElse(null);

            if (client == null)
                return ResponseEntity.status(404).body("Cliente não encontrado!");

            account.setClient(client);
        }

        // Atualizar open (se vier no JSON)
        if (request.getOpen() != null) {
            account.setOpen(request.getOpen());
        }

        // Salvar atualizações
        accountDAO.save(account);

        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/accounts")
    public void deleteAllAccounts() // Delete All account on database
    {
        accountDAO.deleteAll();
    }

    @DeleteMapping("/account/{id}")
    public void deleteAccount(@PathVariable int id) // Delete account by id
    {
        accountDAO.deleteById(id);
    }

    @GetMapping("/account/tip/{id}")
    public ResponseEntity<?> getTip(@PathVariable int id) throws IOException {
        return ResponseEntity.ok().body(tipCalculationService.tipCalculation(id));
    }

    @GetMapping("/account/value/{id}")
    public ResponseEntity<?> getAccountValue(@PathVariable int id) throws IOException {
        return ResponseEntity.ok().body(accountCalculationValueService.accountCalculation(id));
    }

    @GetMapping("/accounts/{cpf}")
    public ArrayList<Account> getClientAccounts(@PathVariable String cpf)
    {
        return accountDAO.findByClientCpf(cpf);
    }

    @PostMapping("/account/open")
    public ResponseEntity<?> openAccount(@RequestBody AccountOpenRequest request)
    {
        try {
            if(!validatedAccountService.validateOpenAccount(request.getCpf_client()))
                return ResponseEntity.status(404).body("O cliente já tem uma conta aberta!");

            Client client = clientDAO.findById(request.getCpf_client()).orElse(null);

            if (client == null)
                return ResponseEntity.status(404).body("O cliente não existe no banco de dados!");

            Account account = new Account();

            account.setClient(client);
            account.setOpen(true);
            account.setAccountId(0);
            account.setPeoples(request.getPeoples());

            accountDAO.save(account);

            Account newAccount = openAccountService.getAccountOpen(client.getCpf());
            Item item = itemDAO.findById(0).orElse(null);

            if (item == null) {
                // Cadastra ingresso se ele ainda nãi tiver sido cadastrado
                item = new Item();
                item.setName("Ingresso");
                item.setNumber_item(0);
                item.setAvailable(true);
                item.setType(0);
                item.setValue(0);

                itemDAO.save(item);
            }

            if (request.isCouvert()) {
                Consumption couvert = new Consumption();

                couvert.setAccount(newAccount);
                couvert.setItem(item);
                couvert.setQuantity(request.getPeoples());

                consumptionDAO.save(couvert);
            }

            return ResponseEntity.ok(accountDAO.save(newAccount));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Erro: " + e);
        }
    }


    @PutMapping("/account/close/{accountId}")
    public ResponseEntity<?> accountClose(@PathVariable int accountId) throws IOException {

        try {
            Account account = accountDAO.findById(accountId).orElse(null);
            double value = accountCalculationValueService.accountCalculation(accountId);
            TipValuesRequest tips = tipCalculationService.tipCalculation(accountId);

            if (account == null) {
                return ResponseEntity.status(404).body("Conta não existe!");
            }

            // Fecha a conta e adciona informacoes fondamentais
            account.setOpen(false);
            account.setDate_close(LocalDateTime.now());
            account.setValue(value);
            account.setTipDrink(tips.getTipDrinkValue());
            account.setTipFood(tips.getTipFoodValue());

            // Salvar atualizações
            accountDAO.save(account);

            return ResponseEntity.ok(account);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Erro:" + e);
        }

    }

    @PostMapping("/account/values/update")
    public ResponseEntity<?> accountValueUpdate(@PathVariable int accountId) throws IOException
    {
        try{
            Account account = accountDAO.findById(accountId).orElse(null);

            if(account == null)
                return ResponseEntity.status(404).body("Essa conta não existe!");

            if(account.isOpen())
                return ResponseEntity.status(404).body("Conta ainda está aberta!");

            double value = accountCalculationValueService.accountCalculation(accountId);
            TipValuesRequest tips = tipCalculationService.tipCalculation(accountId);

            account.setValue(value);
            account.setTipFood(tips.getTipFoodValue());
            account.setTipDrink(tips.getTipFoodValue());

            accountDAO.save(account);

            return ResponseEntity.ok(account);
        }catch (Exception e)
        {
            return ResponseEntity.status(500).body("Erro: " + e);
        }
    }

}