package br.com.mybar.project.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.mybar.project.model.Account;
import br.com.mybar.project.model.AccountItem;
import br.com.mybar.project.model.Actors.User;
import br.com.mybar.project.model.DefinedTypes.AccountStatus;
import br.com.mybar.project.repository.AccountItemRepositoryInterface;
import br.com.mybar.project.repository.AccountRepositoryInterface;
import br.com.mybar.project.repository.UserRepositoryInterface;
import br.com.mybar.project.model.DataTransferObject.AccountClosingDTO;

import java.math.BigDecimal;
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

    @Autowired
    private PaymentService pagamentoService;

    @Autowired
    private AccountItemRepositoryInterface iItemConta;

    @Transactional
    public Account abrirConta(Account novaConta, String codigoGarcom, String senha) {

        User garcom = usuarioService.autenticarGarcom(codigoGarcom, senha);
        novaConta.setGarconAbertura(usuarioRepository.getReferenceById(garcom.getCodigo()));

        boolean cpfComContaAberta = iConta.existsByCliente_CpfAndStatus(
                novaConta.getCliente().getCpf(),
                AccountStatus.ABERTA
        );
        if (cpfComContaAberta) {
            throw new IllegalStateException("Já existe uma conta aberta para este CPF.");
        }

        boolean numeroEmUso = iConta.existsByNumeroAndStatus(
                novaConta.getNumero(),
                AccountStatus.ABERTA
        );
        if (numeroEmUso) {
            throw new IllegalStateException("Este número de conta já está em uso.");
        }

        novaConta.setStatus(AccountStatus.ABERTA);
        novaConta.setDataAbertura(LocalDate.now());
        novaConta.setHoraAbertura(LocalTime.now());

        return iConta.save(novaConta);
    }

    public Account buscarConta(Long id) {
        return iConta.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada."));
    }

    @Transactional
    public Account alterarConta(Account contaAlterada, String codigoGarcom, String senha) {

        User garcom = usuarioService.autenticarGarcom(codigoGarcom, senha);

        Account contaExistente = buscarConta(contaAlterada.getId());

        boolean numeroEmUso = iConta.existsByNumeroAndStatusAndIdNot(
                contaAlterada.getNumero(),
                AccountStatus.ABERTA,
                contaAlterada.getId()
        );
        if (numeroEmUso) {
            throw new IllegalStateException("Este número de conta já está em uso.");
        }

        contaExistente.setNumero(contaAlterada.getNumero());
        contaExistente.setCliente(contaAlterada.getCliente());
        contaExistente.setGarconAbertura(usuarioRepository.getReferenceById(garcom.getCodigo()));

        return iConta.save(contaExistente);
    }

    public List<Account> listarContas() {
        return iConta.findAll();
    }
    @Transactional
    public void excluirConta(Long id) {

        Account conta = buscarConta(id);


        iConta.delete(conta);
    }

    public Long contarContas()
    {
        return iConta.count();
    }

    @Transactional
    public AccountClosingDTO fecharConta(Long id, String codigoGarcom, String senha) {

        // 1. VALIDAR — senha do garçom (só valida, não precisa do User)
        usuarioService.verificarSenhaGarcom(codigoGarcom, senha);

        // 2. BUSCAR — conta existe e está aberta?
        Account conta = buscarConta(id);
        if (conta.getStatus() != AccountStatus.ABERTA) {
            throw new IllegalStateException("Conta não está aberta.");
        }

        // 3. BUSCAR — itens ativos da conta (o ingresso já entra aqui como um item normal,
        // pois RF5 diz que ele é lançado na abertura da conta)
        List<AccountItem> itens = iItemConta.findByContaIdAndAtivoTrue(id);

        // 4. PROCESSAR — soma dos itens (sem alterar o preço travado no lançamento) e gorjeta informativa
        BigDecimal valorTotalItens = BigDecimal.ZERO;
        BigDecimal valorGorjeta = BigDecimal.ZERO;

        for (AccountItem item : itens) {
            BigDecimal valorItem = item.getItemCardapio().getValor()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()));
            valorTotalItens = valorTotalItens.add(valorItem);

            BigDecimal percentualGorjeta = item.getItemCardapio().getTipoItem().getGorjeta();
            BigDecimal gorjetaItem = valorItem
                    .multiply(percentualGorjeta)
                    .divide(BigDecimal.valueOf(100));
            valorGorjeta = valorGorjeta.add(gorjetaItem);
        }

        BigDecimal valorTotalComGorjeta = valorTotalItens.add(valorGorjeta);

        // 5. VALIDAR — soma dos pagamentos (podem ser vários meios diferentes) confere com o total?
        BigDecimal valorPago = pagamentoService.somarPagamentos(id);
        if (valorPago.compareTo(valorTotalComGorjeta) != 0) {
            throw new IllegalStateException(
                    "A soma dos pagamentos (R$ " + valorPago +
                    ") não confere com o valor total da conta (R$ " + valorTotalComGorjeta + ").");
        }

        // 6. SALVAR — muda o status da conta para fechada
        conta.setStatus(AccountStatus.FECHADA);
        iConta.save(conta);

        // 7. RETORNAR — objeto completo com o breakdown do fechamento
        return new AccountClosingDTO(
                conta,
                itens,
                valorTotalItens,
                valorGorjeta,
                valorTotalComGorjeta,
                valorPago,
                pagamentoService.listarPagamentos(id)
        );
    }
}
