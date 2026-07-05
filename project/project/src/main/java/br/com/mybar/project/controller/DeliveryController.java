package br.com.mybar.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.mybar.project.model.AccountItem;
import br.com.mybar.project.model.Actors.User;
import br.com.mybar.project.repository.AccountItemRepositoryInterface;
import br.com.mybar.project.service.UserService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/entregas")
public class DeliveryController {
    @Autowired
    private UserService usuarioService;

    @Autowired
    private AccountItemRepositoryInterface iItemConta;

    @GetMapping
    public ResponseEntity<List<AccountItem>> listar() {
        return ResponseEntity.ok(
                iItemConta.findByAtivoTrueAndDataEntregaCozinhaIsNotNull()
        );
    }

    @PutMapping("/{id}/receber")
    public ResponseEntity<AccountItem> receberNoBar(@PathVariable Long id) {
        AccountItem item = iItemConta.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado."));
        item.setDataRecebimentoBar(LocalDate.now());
        item.setHoraRecebimentoBar(LocalTime.now());
        return ResponseEntity.ok(iItemConta.save(item));
    }

    @PutMapping("/{id}/entregar")
    public ResponseEntity<AccountItem> entregarAoGarcom(
            @PathVariable Long id,
            @RequestParam String codigoAtendente,
            @RequestParam String senha,
            @RequestParam String codigoGarcom) {

        usuarioService.verificarSenhaGarcom(codigoAtendente, senha);

        AccountItem item = iItemConta.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado."));

        if (item.getDataRecebimentoBar() == null) {
            throw new IllegalStateException("Item precisa ser recebido no bar antes de ser entregue.");
        }

        User garcomQueVaiLevar = usuarioService.buscarPorCodigo(codigoGarcom);

        item.setDataEntregaBar(LocalDate.now());
        item.setHoraEntregaBar(LocalTime.now());
        item.setGarcomEntrega(garcomQueVaiLevar);

        return ResponseEntity.ok(iItemConta.save(item));
    }
}