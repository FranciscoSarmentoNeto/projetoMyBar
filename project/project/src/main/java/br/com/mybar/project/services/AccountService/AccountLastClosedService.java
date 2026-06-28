package br.com.mybar.project.services.AccountService;

import br.com.mybar.project.DAOs.AccountInterface;
import br.com.mybar.project.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AccountLastClosedService {
    @Autowired
    private AccountInterface accountDAO;

    // Retorna a ultima conta fechada
    public Account accountLastClosed(String cpf)
    {
        ArrayList<Account> accounts = accountDAO.findByClientCpf(cpf);

        if (accounts == null || accounts.isEmpty()) {
            return null;
        }

        Account lastAccount = null;
        boolean flag = false;

        for (Account account : accounts) {
            if(!flag && account != null && !account.isOpen())
            {
                flag = true;
                lastAccount = account;
            }
            else if (account != null && !account.isOpen()) { // só contas fechadas
                if (account.getDate_close().isAfter(lastAccount.getDate_close())) {
                    lastAccount = account; // atualiza para a mais recente
                }
            }
        }

        return lastAccount;
    }
}
