package br.com.mybar.project.services.AccountService;

import br.com.mybar.project.DAOs.AccountInterface;
import br.com.mybar.project.DAOs.ConsumptionInterface;
import br.com.mybar.project.services.PayService.PaymentFullAccountService;
import br.com.mybar.project.models.Account;
import br.com.mybar.project.models.Consumption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class AccountCalculationValueService {
    @Autowired
    private AccountInterface accountDAO;
    @Autowired
    private ConsumptionInterface consumptionDAO;
    @Autowired
    AccountCalculationConsumptionsService accountCalculationValueService;
    @Autowired
    PaymentFullAccountService paymentFullAccountService;


    public Double accountCalculation(int accountId) throws IOException {
        Account account = accountDAO.findById(accountId).orElse(null);

        ArrayList<Consumption> accountConsumptions = consumptionDAO.findByAccountId(accountId);
        double valueAccount =  accountCalculationValueService.accountCalculationConsumptions(accountId);
        double payAccount = paymentFullAccountService.paymentFullAccountServe(accountId);

        return valueAccount - payAccount;
    }
}
