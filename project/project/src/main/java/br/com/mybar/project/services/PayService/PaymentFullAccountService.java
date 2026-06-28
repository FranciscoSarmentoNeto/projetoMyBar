package br.com.mybar.project.services.PayService;

import br.com.mybar.project.DAOs.PayInterface;
import br.com.mybar.project.models.Pay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PaymentFullAccountService {

    @Autowired
    private PayInterface payDAO;

    // Calcula o valor dos pagamentos ja realizados
    public double paymentFullAccountServe(int accountId)
    {
        ArrayList<Pay> paymentsAccount = payDAO.findByAccountId(accountId);

        double payFull = 0.0;

        for(Pay pay: paymentsAccount)
            payFull += pay.getValue();

        return payFull;
    }

}
