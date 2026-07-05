package br.com.mybar.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.mybar.project.model.AccountItem;
import br.com.mybar.project.repository.AccountItemRepositoryInterface;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/cozinha")
public class kitchenController {

    @Autowired
    private AccountItemRepositoryInterface iItemConta;

    @GetMapping
    public ResponseEntity<List<AccountItem>> listar() {
        return ResponseEntity.ok(iItemConta.findByAtivoTrueAndItemCardapio_TipoItem_CozinhaTrue());
    }

    @PutMapping("/{id}/receber")
    public ResponseEntity<AccountItem> receber(@PathVariable Long id) {
        AccountItem item = iItemConta.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado."));
        item.setDataRecebimentoCozinha(LocalDate.now());
        item.setHoraRecebimentoCozinha(LocalTime.now());
        return ResponseEntity.ok(iItemConta.save(item));
    }

    @PutMapping("/{id}/entregar")
    public ResponseEntity<AccountItem> entregar(@PathVariable Long id) {
        AccountItem item = iItemConta.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado."));
        item.setDataEntregaCozinha(LocalDate.now());
        item.setHoraEntregaCozinha(LocalTime.now());
        return ResponseEntity.ok(iItemConta.save(item));
    }
}