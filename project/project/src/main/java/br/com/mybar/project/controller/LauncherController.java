package br.com.mybar.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.mybar.project.model.AccountItem;
import br.com.mybar.project.service.LaunchService;

import java.util.List;

@RestController
@RequestMapping("/lancamentos")
public class LauncherController {

    @Autowired
    private LaunchService lancamentoService;

    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<AccountItem>> listarItensDaConta(@PathVariable Long contaId) {
        return ResponseEntity.ok(lancamentoService.listarItensDaConta(contaId));
    }

    @PostMapping("/{contaId}")
    public ResponseEntity<AccountItem> lancarItem(
            @PathVariable Long contaId,
            @RequestParam Integer codigoItem,
            @RequestParam Integer quantidade,
            @RequestParam String codigoGarcom,
            @RequestParam String senha) {
        return ResponseEntity.status(201).body(
                lancamentoService.lancarItem(contaId, codigoItem, quantidade, codigoGarcom, senha)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirItem(
            @PathVariable Long id,
            @RequestParam String usuarioAdmin,
            @RequestParam String senha) {
        lancamentoService.excluirItem(id, usuarioAdmin, senha);
        return ResponseEntity.status(204).build();
    }
}