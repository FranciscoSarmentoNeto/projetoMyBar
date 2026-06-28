package br.com.mybar.project.services.PayService;

import br.com.mybar.project.DAOs.AccountInterface;
import br.com.mybar.project.DAOs.ConsumptionInterface;
import br.com.mybar.project.services.AccountService.AccountCalculationConsumptionsService;
import br.com.mybar.project.models.Account;
import br.com.mybar.project.models.Consumption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class PaymentService {

    @Autowired
    private AccountInterface accountDAO;
    @Autowired
    private ConsumptionInterface consumptionDAO;
    @Autowired
    AccountCalculationConsumptionsService accountCalculationValueService;
    @Autowired
    PaymentFullAccountService paymentFullAccountService;

    public boolean validatedPayment(int accountId, double payValue) throws IOException {
        Account account = accountDAO.findById(accountId).orElse(null);

        if (account == null)
            return false;


        double valueAccount = account.getValue();


        double payAccount = paymentFullAccountService.paymentFullAccountServe(accountId);

        return (valueAccount - payAccount >= 0);
    }
}
