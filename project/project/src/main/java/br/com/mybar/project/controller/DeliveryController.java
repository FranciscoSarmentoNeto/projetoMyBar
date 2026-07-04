package br.com.mybar.project.controller;

import br.com.mybar.project.model.DataTransferObject.FinalizarEntregaDTO;
import br.com.mybar.project.model.ItemConta;
import br.com.mybar.project.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    /**
     * Endpoint para o atendente indicar que iniciou o preparo/separação do item no balcão.
     * PUT /api/delivery/{id}/iniciar
     */
    @PutMapping("/{id}/iniciar")
    public ResponseEntity<ItemConta> iniciarPreparacao(@PathVariable Long id) {
        try {
            ItemConta item = deliveryService.iniciarPreparacaoNoBalcao(id);
            return ResponseEntity.ok(item);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint para finalizar e dar baixa na entrega do item.
     * Exige a validação por código e senha do atendente no corpo da requisição.
     * PUT /api/delivery/{id}/finalizar
     */
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<ItemConta> finalizarEntrega(
            @PathVariable Long id, 
            @RequestBody FinalizarEntregaDTO dto) {
        try {
            ItemConta item = deliveryService.finalizarEntregaNoBalcao(
                    id, 
                    dto.codigoAtendente(), 
                    dto.senhaAtendente()
            );
            return ResponseEntity.ok(item);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (SecurityException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}