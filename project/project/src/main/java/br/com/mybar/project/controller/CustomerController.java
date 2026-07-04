package br.com.mybar.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.mybar.project.model.Actors.Client;
import br.com.mybar.project.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin("*")
public class CustomerController {

    @Autowired
    private CustomerService clienteService;

    @GetMapping
    public ResponseEntity<List<Client>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @PostMapping
    public ResponseEntity<Client> incluirCliente(@RequestBody Client cliente) {
        Client novo = clienteService.incluirCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> editarCliente(@PathVariable Long id, @RequestBody Client cli) {
        Client atualizado = clienteService.editarCliente(id, cli);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Client> buscarPorCpf(@PathVariable String cpf) {
        return clienteService.findCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
