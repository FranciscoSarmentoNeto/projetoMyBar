package br.com.mybar.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.mybar.project.model.Account;
import br.com.mybar.project.model.DataTransferObject.AccountClosingDTO;
import br.com.mybar.project.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/contas")
public class AccountController {

    @Autowired
    private AccountService contaService;

    @PostMapping
    public ResponseEntity<Account> abrirConta(
            @RequestBody Account novaConta,
            @RequestParam String codigoGarcom,
            @RequestParam String senha) {

        return ResponseEntity.status(201)
                .body(contaService.abrirConta(novaConta, codigoGarcom, senha));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> buscarConta(@PathVariable Long id) {
        return ResponseEntity.ok(contaService.buscarConta(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> alterarConta(
            @PathVariable Long id,
            @RequestBody Account conta,
            @RequestParam String codigoGarcom,
            @RequestParam String senha) {

        conta.setId(id);
        return ResponseEntity.ok(contaService.alterarConta(conta, codigoGarcom, senha));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirConta(@PathVariable Long id) {
        contaService.excluirConta(id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping
    public ResponseEntity<List<Account>> listarContas() {
        return ResponseEntity.ok(contaService.listarContas());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> contarContas()
    {
        return ResponseEntity.status(200).body(contaService.contarContas());
    }

    @PutMapping("/{id}/fechar")
    public ResponseEntity<AccountClosingDTO> fecharConta(
            @PathVariable Long id,
            @RequestParam String codigoGarcom,
            @RequestParam String senha) {

        return ResponseEntity.ok(contaService.fecharConta(id, codigoGarcom, senha));
    }
}