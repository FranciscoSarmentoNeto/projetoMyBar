package br.com.mybar.project.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import br.com.mybar.project.model.AccountItem;
import br.com.mybar.project.model.DefinedTypes.UserType;
import br.com.mybar.project.model.Actors.User;
import br.com.mybar.project.repository.AccountItemRepositoryInterface;
import br.com.mybar.project.repository.UserRepositoryInterface;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class CounterService {

    @Autowired
    private AccountItemRepositoryInterface iItemConta;

    @Autowired
    private UserRepositoryInterface iUsuario;

    @Transactional
    public AccountItem prepararPedido(Long idItemConta) {
        AccountItem item = iItemConta.findById(idItemConta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado."));

        item.setDataRecebimentoBar(LocalDate.now());
        item.setHoraRecebimentoBar(LocalTime.now());

        return iItemConta.save(item);
    }

    @Transactional
    public AccountItem registrarEntregaGarcom(Long idItemConta, Integer codigoGarcom) {
        AccountItem item = iItemConta.findById(idItemConta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado."));

        User garcom = iUsuario.findById(codigoGarcom)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código de garçom inválido."));

        if (!garcom.getTipo().equals(UserType.GARCOM)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "O código informado não pertence a um garçom.");
        }

        item.setDataEntregaBar(LocalDate.now());
        item.setHoraEntregaBar(LocalTime.now());
        item.setGarcomEntrega(garcom);

        return iItemConta.save(item);
    }
}