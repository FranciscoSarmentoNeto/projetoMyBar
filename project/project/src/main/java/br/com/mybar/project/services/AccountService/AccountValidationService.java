package br.com.mybar.project.services.AccountService;

import br.com.mybar.project.DAOs.AccountInterface;
import br.com.mybar.project.DAOs.ClientInterface;
import br.com.mybar.project.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AccountValidationService {

    @Autowired
    private AccountInterface accountDAO;

    @Autowired
    private ClientInterface clientDAO;

    // Verifica se o cliente ja tem alguma conta aberta
    public boolean validateOpenAccount(String cpf)
    {
        ArrayList<Account> accountsClient = accountDAO.findByClientCpf(cpf);

        for (Account account: accountsClient)
            if(account.isOpen())
                return false;

        return true;
    }
}
