package br.com.mybar.project.repository;

import br.com.mybar.project.model.AccountItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountItemRepositoryInterface extends JpaRepository<AccountItem, Long> {

    boolean existsByItemCardapioCodigo(Integer codigoItemCardapio);

    List<AccountItem> findByContaIdAndAtivoTrue(Long contaId);

    List<AccountItem> findByAtivoTrueAndItemCardapio_TipoItem_CozinhaTrue();

    List<AccountItem> findByAtivoTrueAndDataEntregaCozinhaIsNotNull();
}