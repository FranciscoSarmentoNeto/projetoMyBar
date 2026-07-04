package br.com.mybar.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.mybar.project.model.Config;
import br.com.mybar.project.model.DefinedTypes.OperationMode;
import br.com.mybar.project.service.SettingsService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/configuracao")
public class SettingsController {
    private SettingsService configuracaoService;

    public SettingsController(SettingsService configuracaoService){
        this.configuracaoService = configuracaoService;
    }

    @GetMapping
    public ResponseEntity<Config> buscarAtual()
    {
        return ResponseEntity.status(200).body(configuracaoService.getConfig());
    }
    @PutMapping("/ingresso")
    public ResponseEntity<Config> atualizarIngresso(@RequestParam BigDecimal masc, @RequestParam BigDecimal fem)
    {
        return ResponseEntity.status(201).body(configuracaoService.atualizarValoresIngresso(masc, fem));
    }

    @PutMapping("/operation")
    public ResponseEntity<Config> atualizarOperacao(@RequestParam OperationMode op){
        return ResponseEntity.status(201).body(configuracaoService.alterarModoOperacao(op));
    }

}
