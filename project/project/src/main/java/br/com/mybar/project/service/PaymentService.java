package br.com.mybar.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.mybar.project.model.Account;
import br.com.mybar.project.model.Payment;
import br.com.mybar.project.model.DefinedTypes.AccountStatus;
import br.com.mybar.project.repository.AccountRepositoryInterface;
import br.com.mybar.project.repository.PaymentRepositoryInterface;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepositoryInterface iPagamento;

    @Autowired
    private AccountRepositoryInterface iConta;

    @Autowired
    private UserService usuarioService;

    // RF3 — registra um pagamento na conta
    public Payment registrarPagamento(Long contaId, Payment pagamento,
                                        String codigoGarcom, String senha) {
        // 1. VALIDAR — senha do garçom
        usuarioService.verificarSenhaGarcom(codigoGarcom, senha);

        // 2. BUSCAR — conta existe e está aberta?
        Account conta = iConta.findById(contaId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada."));

        if (conta.getStatus() != AccountStatus.ABERTA) {
            throw new IllegalStateException("Conta não está aberta.");
        }

        // 3. PROCESSAR — vincula o pagamento à conta
        pagamento.setConta(conta);

        // 4. SALVAR
        return iPagamento.save(pagamento);
    }

    // RF3 — exclui pagamento com senha de admin
    public void excluirPagamento(Long pagamentoId, String usuarioAdmin, String senha) {
        // 1. VALIDAR — senha do admin
        usuarioService.verificarSenhaAdmin(usuarioAdmin, senha);

        // 2. BUSCAR — pagamento existe?
        Payment pagamento = iPagamento.findById(pagamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado."));


        iPagamento.delete(pagamento);
    }


    public BigDecimal somarPagamentos(Long contaId) {
        List<Payment> pagamentos = iPagamento.findByConta_Id(contaId);
        return pagamentos.stream()
                .map(Payment::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}