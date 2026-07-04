package br.com.mybar.project.service;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import br.com.mybar.project.model.Config;
import br.com.mybar.project.model.DefinedTypes.OperationMode;
import br.com.mybar.project.repository.SettingsRepositoryInterface;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class SettingsService {
    private SettingsRepositoryInterface repository;

    public SettingsService(SettingsRepositoryInterface repository){
        this.repository = repository;
    }

    public Config getConfig(){
        return repository.findById(1).orElseGet(() -> {
            Config configInicial = new Config();
            configInicial.setValorIngressoMasc(new BigDecimal("50.00"));
            configInicial.setValorIngressoFemin(new BigDecimal("10.00"));
            configInicial.setModoOperacao(OperationMode.GESTAO);
            return repository.save(configInicial);
        });
    }
    @Transactional
    public Config atualizarValoresIngresso(BigDecimal masc, BigDecimal fem) {
    Config config = getConfig();
    config.setValorIngressoMasc(masc);
    config.setValorIngressoFemin(fem);
    return repository.save(config);
    }

    @Transactional
    public Config alterarModoOperacao(OperationMode novoModo) {
        Config config = getConfig();
        config.setModoOperacao(novoModo);
        config.setData(LocalDate.now());
        config.setHora(LocalTime.now());
        return repository.save(config);
    }
}
