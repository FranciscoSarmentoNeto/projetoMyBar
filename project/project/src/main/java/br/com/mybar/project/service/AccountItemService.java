package br.com.mybar.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.mybar.project.model.Account;
import br.com.mybar.project.model.AccountItem;
import br.com.mybar.project.model.MenuItem;
import br.com.mybar.project.model.Actors.User;
import br.com.mybar.project.model.DataTransferObject.ItemEntryDTO;
import br.com.mybar.project.model.DefinedTypes.AccountStatus;
import br.com.mybar.project.repository.AccountItemRepositoryInterface;
import br.com.mybar.project.repository.AccountRepositoryInterface;
import br.com.mybar.project.repository.MenuItemRepositoryInterface;
import br.com.mybar.project.repository.UserRepositoryInterface;

import java.util.List;

@Service
public class AccountItemService {

    @Autowired
    private AccountItemRepositoryInterface iItemConta;

    @Autowired
    private AccountRepositoryInterface iConta;

    @Autowired
    private MenuItemRepositoryInterface iItemCardapio;

    @Autowired
    private UserRepositoryInterface iUsuario;

    @Autowired
    private UserService usuarioService;

    @Transactional
    public AccountItem lancarItem(Long contaId, ItemEntryDTO dados, String codigoGarcom, String senha) {

        User garcom = usuarioService.autenticarGarcom(codigoGarcom, senha);

        Account conta = iConta.findById(contaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (conta.getStatus() != AccountStatus.ABERTA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conta não está aberta.");
        }

        MenuItem itemCardapio = iItemCardapio.findById(dados.codigoItemCardapio())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item de cardápio não encontrado."));

        if (!itemCardapio.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item de cardápio não está mais disponível.");
        }

        if (dados.quantidade() == null || dados.quantidade() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade deve ser maior que zero.");
        }

        AccountItem itemConta = new AccountItem();
        itemConta.setConta(conta);
        itemConta.setItemCardapio(itemCardapio);
        itemConta.setQuantidade(dados.quantidade());
        itemConta.setAtivo(true);
        itemConta.setQuemLancou(iUsuario.getReferenceById(garcom.getCodigo()));

        return iItemConta.save(itemConta);
    }

    @Transactional
    public void excluirItem(Long itemContaId, String usuarioAdmin, String senha) {

        usuarioService.verificarSenhaAdmin(usuarioAdmin, senha);

        AccountItem itemConta = iItemConta.findById(itemContaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado na conta."));

        if (!itemConta.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este item já foi removido da conta.");
        }

        UserDetails adminDetails = iUsuario.findByEmail(usuarioAdmin);
        User admin = (User) adminDetails;

        itemConta.setAtivo(false);
        itemConta.setQuemRemoveu(iUsuario.getReferenceById(admin.getCodigo()));
        iItemConta.save(itemConta);
    }

    public List<AccountItem> listarItensDaConta(Long contaId) {
        return iItemConta.findByContaIdAndAtivoTrue(contaId);
    }
}