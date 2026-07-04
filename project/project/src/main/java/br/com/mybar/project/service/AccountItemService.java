package br.com.mybar.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.mybar.project.model.Account;
import br.com.mybar.project.model.ItemConta;
import br.com.mybar.project.model.MenuItem;
import br.com.mybar.project.model.Actors.User;
import br.com.mybar.project.model.DataTransferObject.ItemLancamentoDTO;
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

    // RF2 — lança um item do cardápio em uma conta (fluxo alternativo "Lançamento de Item em Conta")
    @Transactional
    public ItemConta lancarItem(Long contaId, ItemLancamentoDTO dados, String codigoGarcom, String senha) {

        // 1. VALIDAR — senha do garçom
        User garcom = usuarioService.autenticarGarcom(codigoGarcom, senha);

        // 2. BUSCAR — conta existe e está aberta?
        Account conta = iConta.findById(contaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (conta.getStatus() != AccountStatus.ABERTA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conta não está aberta.");
        }

        // 3. BUSCAR — item do cardápio existe e está ativo?
        MenuItem itemCardapio = iItemCardapio.findById(dados.codigoItemCardapio())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item de cardápio não encontrado."));

        if (!itemCardapio.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item de cardápio não está mais disponível.");
        }

        if (dados.quantidade() == null || dados.quantidade() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade deve ser maior que zero.");
        }

        // 4. PROCESSAR — monta o lançamento
        ItemConta itemConta = new ItemConta();
        itemConta.setConta(conta);
        itemConta.setItemCardapio(itemCardapio);
        itemConta.setQuantidade(dados.quantidade());
        itemConta.setAtivo(true);
        itemConta.setQuemLancou(iUsuario.getReferenceById(garcom.getCodigo()));

        // 5. SALVAR
        return iItemConta.save(itemConta);
    }

    // RF2 — remove (soft delete) um item lançado na conta, exige autorização de administrador
    @Transactional
    public void excluirItem(Long itemContaId, String usuarioAdmin, String senha) {

        // 1. VALIDAR — senha do administrador
        usuarioService.verificarSenhaAdmin(usuarioAdmin, senha);

        // 2. BUSCAR — item lançado existe?
        ItemConta itemConta = iItemConta.findById(itemContaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado na conta."));

        if (!itemConta.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este item já foi removido da conta.");
        }

        // 3. PROCESSAR — soft delete + rastreabilidade de quem removeu
        UserDetails adminDetails = iUsuario.findByEmail(usuarioAdmin);
        User admin = (User) adminDetails;

        itemConta.setAtivo(false);
        itemConta.setQuemRemoveu(iUsuario.getReferenceById(admin.getCodigo()));

        // 4. SALVAR
        iItemConta.save(itemConta);
    }

    // Lista os itens ativos lançados em uma conta
    public List<ItemConta> listarItensDaConta(Long contaId) {
        return iItemConta.findByContaIdAndAtivoTrue(contaId);
    }
}