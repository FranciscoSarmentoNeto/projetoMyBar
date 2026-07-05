package br.com.mybar.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.mybar.project.service.PaymentService;
import br.com.mybar.project.model.Payment;
import br.com.mybar.project.repository.PaymentRepositoryInterface;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PaymentController {

    @Autowired
    private PaymentRepositoryInterface iPagamento;

    @Autowired
    private PaymentService pagamentoService;

    @PostMapping("/{contaId}")
    public ResponseEntity<Payment> registrarPagamento(
            @PathVariable Long contaId,
            @RequestBody Payment pagamento,
            @RequestParam String codigoGarcom,
            @RequestParam String senha) {

        return ResponseEntity.status(201)
                .body(pagamentoService.registrarPagamento(contaId, pagamento, codigoGarcom, senha));
    }

    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<Payment>> listarPorConta(@PathVariable Long contaId) {
        return ResponseEntity.ok(iPagamento.findByConta_Id(contaId));
    }

    @DeleteMapping("/{pagamentoId}")
    public ResponseEntity<?> excluirPagamento(
            @PathVariable Long pagamentoId,
            @RequestParam String usuarioAdmin,
            @RequestParam String senha) {

        pagamentoService.excluirPagamento(pagamentoId, usuarioAdmin, senha);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/{contaId}/total")
    public ResponseEntity<BigDecimal> totalPagamentos(@PathVariable Long contaId) {
        return ResponseEntity.ok(pagamentoService.somarPagamentos(contaId));
    }
}