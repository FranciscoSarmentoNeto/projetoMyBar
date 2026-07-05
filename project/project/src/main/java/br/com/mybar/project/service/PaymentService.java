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

    public Payment registrarPagamento(Long contaId, Payment pagamento,
                                        String codigoGarcom, String senha) {
        usuarioService.verificarSenhaGarcom(codigoGarcom, senha);

        Account conta = iConta.findById(contaId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada."));

        if (conta.getStatus() != AccountStatus.ABERTA) {
            throw new IllegalStateException("Conta não está aberta.");
        }

        pagamento.setConta(conta);

        return iPagamento.save(pagamento);
    }

    public void excluirPagamento(Long pagamentoId, String usuarioAdmin, String senha) {
        usuarioService.verificarSenhaAdmin(usuarioAdmin, senha);

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

    public List<Payment> listarPagamentos(Long contaId) {
        return iPagamento.findByConta_Id(contaId);
    }
}