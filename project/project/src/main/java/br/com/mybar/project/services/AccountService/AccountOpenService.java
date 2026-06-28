package br.com.mybar.project.services.AccountService;

import br.com.mybar.project.DAOs.AccountInterface;
import br.com.mybar.project.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AccountOpenService {

    @Autowired
    private AccountInterface accountDAO;

    // Acha a primeira conta aberta do cliente
    public Account getAccountOpen(String cpf)
    {
        ArrayList<Account> accounts = accountDAO.findByClientCpf(cpf);

        for(Account account: accounts)
        {
            if(account.isOpen())
                return account;
        }

        return null;
    }

}
