package br.com.mybar.project.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.mybar.project.model.*;
import br.com.mybar.project.model.Actors.User;
import br.com.mybar.project.repository.AccountRepositoryInterface;
import br.com.mybar.project.repository.MenuItemRepositoryInterface;
import br.com.mybar.project.repository.AccountItemRepositoryInterface;
import br.com.mybar.project.repository.UserRepositoryInterface;
import br.com.mybar.project.model.DefinedTypes.AccountStatus;


import java.util.List;

@Service
public class LaunchService {

    @Autowired
    private AccountItemRepositoryInterface itemContaRepository;

    @Autowired
    private AccountRepositoryInterface iConta;

    @Autowired
    private MenuItemRepositoryInterface iItemCardapio;

    @Autowired
    private UserService usuarioService;

    @Autowired
    private UserRepositoryInterface interfaceUsuario;

    @Transactional
    public AccountItem lancarItem(Long contaId, Integer codigoItem,
                                Integer quantidade, String codigoGarcom,
                                String senha) {

        usuarioService.verificarSenhaGarcom(codigoGarcom, senha);

        int codigoInt = Integer.parseInt(codigoGarcom);
        User garcom = interfaceUsuario.findByCodigo(codigoInt)
                .orElseThrow(() -> new IllegalArgumentException("Garçom não encontrado."));

        Account conta = iConta.findById(contaId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada."));

        if (conta.getStatus() != AccountStatus.ABERTA) {
            throw new IllegalStateException("Conta não está aberta.");
        }

        MenuItem item = iItemCardapio.findById(codigoItem)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado."));

        if (!item.getAtivo()) {
            throw new IllegalStateException("Item não está disponível.");
        }

        AccountItem itemConta = new AccountItem();
        itemConta.setConta(conta);
        itemConta.setItemCardapio(item);
        itemConta.setQuantidade(quantidade);
        itemConta.setAtivo(true);
        itemConta.setQuemLancou(garcom);
        return itemContaRepository.save(itemConta);
    }

    @Transactional
    public void excluirItem(Long itemContaId, String usuarioAdmin, String senha) {

        usuarioService.verificarSenhaAdmin(usuarioAdmin, senha);

        AccountItem itemConta = itemContaRepository.findById(itemContaId)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado."));

            itemConta.setAtivo(false);
            itemContaRepository.save(itemConta);
    }

    public List<AccountItem> listarItensDaConta(Long contaId) {
        return itemContaRepository.findByContaIdAndAtivoTrue(contaId);
    }
}