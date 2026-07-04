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

    // GET /api/tipos-item?descricao=Bebida
    @GetMapping
    public ResponseEntity<List<ItemType>> listar(@RequestParam(required = false) String descricao) {
        return ResponseEntity.ok(tipoItemService.pesquisar(descricao));
    }

    // GET /api/tipos-item/10
    @GetMapping("/{codigo}")
    public ResponseEntity<ItemType> buscarPorCodigo(@PathVariable Integer codigo) {
        return ResponseEntity.ok(tipoItemService.buscarPorCodigo(codigo));
    }

    // POST /api/tipos-item
    @PostMapping
    public ResponseEntity<ItemType> criar(@RequestBody ItemType tipoItem) {
        // Força a classe a ser entendida como nova para garantir o INSERT
        tipoItem.marcarComoAntigo(); // Reset se necessário, mas o padrão já é true na criação
        ItemType novoTipo = tipoItemService.salvar(tipoItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoTipo);
    }

    // PUT /api/tipos-item/10
    @PutMapping("/{codigo}")
    public ResponseEntity<ItemType> atualizar(@PathVariable Integer codigo, @RequestBody ItemTypeDTO tipoItem) {
        // Garante que o código da URL é o mesmo do objeto a ser alterado
        // tipoItem.setCodigo(codigo);
        // O Hibernate fará o UPDATE porque o ID já existe
        
        return ResponseEntity.ok(tipoItemService.editar(codigo, tipoItem));
    }

    // DELETE /api/tipos-item/10
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> excluir(@PathVariable Integer codigo) {
        tipoItemService.excluir(codigo);
        return ResponseEntity.noContent().build();
    }
}
