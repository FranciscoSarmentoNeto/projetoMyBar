package br.com.mybar.project.controller;

import br.com.mybar.project.model.ItemConta;
import br.com.mybar.project.service.KitchenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kitchen")
public class KitchenController {

    @Autowired
    private KitchenService kitchenService;

    /**
     * UC7: Endpoint para a cozinha indicar que recebeu e iniciou o preparo do pedido.
     * PUT /api/kitchen/{id}/receber
     */
    @PutMapping("/{id}/receber")
    public ResponseEntity<ItemConta> receberPedido(@PathVariable Long id) {
        // O próprio KitchenService já faz as validações e lança ResponseStatusException (404 ou 400)
        ItemConta item = kitchenService.receberPedido(id);
        return ResponseEntity.ok(item);
    }

    /**
     * UC7: Endpoint para a cozinha sinalizar que o pedido está pronto e foi entregue ao balcão/garçom.
     * PUT /api/kitchen/{id}/entregar
     */
    @PutMapping("/{id}/entregar")
    public ResponseEntity<ItemConta> entregarPedido(@PathVariable Long id) {
        // O próprio KitchenService já valida o fluxo e lança ResponseStatusException se houver erro
        ItemConta item = kitchenService.entregarPedido(id);
        return ResponseEntity.ok(item);
    }
}