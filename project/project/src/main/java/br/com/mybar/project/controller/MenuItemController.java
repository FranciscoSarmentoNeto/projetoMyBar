package br.com.mybar.project.controller;

import br.com.mybar.project.model.MenuItem;
import br.com.mybar.project.service.MenuItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itens-cardapio")
public class MenuItemController {

    private final MenuItemService itemCardapioService;

    public MenuItemController(MenuItemService itemCardapioService) {
        this.itemCardapioService = itemCardapioService;
    }

    @GetMapping
    public ResponseEntity<List<MenuItem>> listar(@RequestParam(required = false) String descricao) {
        return ResponseEntity.ok(itemCardapioService.pesquisar(descricao));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<MenuItem> buscarPorCodigo(@PathVariable Integer codigo) {
        return ResponseEntity.ok(itemCardapioService.buscarPorCodigo(codigo));
    }

    @PostMapping
    public ResponseEntity<MenuItem> criar(@RequestBody MenuItem itemCardapio) {
        itemCardapio.marcarComoAntigo(); 
        MenuItem novoItem = itemCardapioService.salvar(itemCardapio);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoItem);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<MenuItem> atualizar(@PathVariable Integer codigo, @RequestBody MenuItem itemCardapio) {
        itemCardapio.setCodigo(codigo);
        MenuItem itemAtualizado = itemCardapioService.salvar(itemCardapio);
        return ResponseEntity.ok(itemAtualizado);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> excluir(@PathVariable Integer codigo) {
        itemCardapioService.excluir(codigo);
        return ResponseEntity.noContent().build();
    }
}
