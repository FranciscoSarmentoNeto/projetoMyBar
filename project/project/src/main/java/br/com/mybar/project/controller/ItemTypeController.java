package br.com.mybar.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.mybar.project.model.ItemType;
import br.com.mybar.project.model.DataTransferObject.ItemTypeDTO;
import br.com.mybar.project.service.ItemTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-item")
public class ItemTypeController {

    @Autowired
    private final ItemTypeService tipoItemService;

    public ItemTypeController(ItemTypeService tipoItemService) {
        this.tipoItemService = tipoItemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemType>> listar(@RequestParam(required = false) String descricao) {
        return ResponseEntity.ok(tipoItemService.pesquisar(descricao));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ItemType> buscarPorCodigo(@PathVariable Integer codigo) {
        return ResponseEntity.ok(tipoItemService.buscarPorCodigo(codigo));
    }

    @PostMapping
    public ResponseEntity<ItemType> criar(@RequestBody ItemType tipoItem) {
        tipoItem.marcarComoAntigo();
        ItemType novoTipo = tipoItemService.salvar(tipoItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoTipo);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<ItemType> atualizar(@PathVariable Integer codigo, @RequestBody ItemTypeDTO tipoItem) {
        
        
        return ResponseEntity.ok(tipoItemService.editar(codigo, tipoItem));
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> excluir(@PathVariable Integer codigo) {
        tipoItemService.excluir(codigo);
        return ResponseEntity.noContent().build();
    }
}
