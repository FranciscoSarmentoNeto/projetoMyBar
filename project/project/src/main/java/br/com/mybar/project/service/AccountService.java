package br.com.mybar.project.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.mybar.project.model.Account;
import br.com.mybar.project.model.Actors.User;
import br.com.mybar.project.model.DefinedTypes.AccountStatus;
import br.com.mybar.project.repository.AccountRepositoryInterface;
import br.com.mybar.project.repository.UserRepositoryInterface;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepositoryInterface iConta;

    @Autowired
    private UserService usuarioService;

    @Autowired
    private UserRepositoryInterface usuarioRepository;

    @Transactional
    public Account abrirConta(Account novaConta, String codigoGarcom, String senha) {

        // 1. VALIDAR — senha do garçom
        User garcom = usuarioService.autenticarGarcom(codigoGarcom, senha);
        novaConta.setGarconAbertura(usuarioRepository.getReferenceById(garcom.getCodigo()));

        // 2. VALIDAR — CPF já tem conta aberta?
        boolean cpfComContaAberta = iConta.existsByCliente_CpfAndStatus(
                novaConta.getCliente().getCpf(),
                AccountStatus.ABERTA
        );
        if (cpfComContaAberta) {
            throw new IllegalStateException("Já existe uma conta aberta para este CPF.");
        }

        // 3. VALIDAR — número do cartão já está em uso?
        boolean numeroEmUso = iConta.existsByNumeroAndStatus(
                novaConta.getNumero(),
                AccountStatus.ABERTA
        );
        if (numeroEmUso) {
            throw new IllegalStateException("Este número de conta já está em uso.");
        }

        // 4. PROCESSAR — preenche dados automáticos
        novaConta.setStatus(AccountStatus.ABERTA);
        novaConta.setDataAbertura(LocalDate.now());
        novaConta.setHoraAbertura(LocalTime.now());

        // 5. SALVAR
        return iConta.save(novaConta);
    }

    public Account buscarConta(Long id) {
        return iConta.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada."));
    }

    @Transactional
    public Account alterarConta(Account contaAlterada, String codigoGarcom, String senha) {

        // 1. VALIDAR — senha do garçom
        User garcom = usuarioService.autenticarGarcom(codigoGarcom, senha);

        // 2. BUSCAR — conta existe?
        Account contaExistente = buscarConta(contaAlterada.getId());

        // 3. VALIDAR — número novo já está em uso em outra conta?
        boolean numeroEmUso = iConta.existsByNumeroAndStatusAndIdNot(
                contaAlterada.getNumero(),
                AccountStatus.ABERTA,
                contaAlterada.getId()
        );
        if (numeroEmUso) {
            throw new IllegalStateException("Este número de conta já está em uso.");
        }

        // 4. PROCESSAR — atualiza campos permitidos
        contaExistente.setNumero(contaAlterada.getNumero());
        contaExistente.setCliente(contaAlterada.getCliente());
        contaExistente.setGarconAbertura(usuarioRepository.getReferenceById(garcom.getCodigo()));

        // 5. SALVAR
        return iConta.save(contaExistente);
    }

    public List<Account> listarContas() {
        return iConta.findAll();
    }
    @Transactional
    public void excluirConta(Long id) {

        // 1. BUSCAR
        Account conta = buscarConta(id);

        // 2. VALIDAR — documento diz: só exclui se não tiver itens
        // quando LancamentoItem existir, adiciona verificação aqui

        // 3. SALVAR
        iConta.delete(conta);
    }

    public Long contarContas()
    {
        return iConta.count();
    }
}
