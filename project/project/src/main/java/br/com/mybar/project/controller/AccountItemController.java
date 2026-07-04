package br.com.mybar.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.mybar.project.model.ItemConta;
import br.com.mybar.project.model.DataTransferObject.ItemLancamentoDTO;
import br.com.mybar.project.service.AccountItemService;

import java.util.List;

@RestController
@RequestMapping("/itens-conta")
public class AccountItemController {

    @Autowired
    private AccountItemService itemContaService;

    @PostMapping("/{contaId}")
    public ResponseEntity<ItemConta> lancarItem(
            @PathVariable Long contaId,
            @RequestBody ItemLancamentoDTO dados,
            @RequestParam String codigoGarcom,
            @RequestParam String senha) {

        return ResponseEntity.status(201)
                .body(itemContaService.lancarItem(contaId, dados, codigoGarcom, senha));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirItem(
            @PathVariable Long id,
            @RequestParam String usuarioAdmin,
            @RequestParam String senha) {

        itemContaService.excluirItem(id, usuarioAdmin, senha);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<ItemConta>> listarItensDaConta(@PathVariable Long contaId) {
        return ResponseEntity.ok(itemContaService.listarItensDaConta(contaId));
    }
}