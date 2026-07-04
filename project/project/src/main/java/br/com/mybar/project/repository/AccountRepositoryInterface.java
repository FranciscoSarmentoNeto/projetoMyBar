package br.com.mybar.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.mybar.project.model.Account;
import br.com.mybar.project.model.DefinedTypes.AccountStatus;

public interface AccountRepositoryInterface extends JpaRepository<Account, Long> {
    boolean existsByCliente_CpfAndStatus(String cpf, AccountStatus status);

    boolean existsByNumeroAndStatus(int numero, AccountStatus status);
    boolean existsByNumeroAndStatusAndIdNot(int numero, AccountStatus status, Long id);
}
