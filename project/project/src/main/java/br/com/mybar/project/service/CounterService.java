package br.com.mybar.project.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import br.com.mybar.project.model.ItemConta;
import br.com.mybar.project.model.Actors.User;
import br.com.mybar.project.model.DefinedTypes.UserType;
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
    public ItemConta prepararPedido(Long idItemConta) {
        ItemConta item = iItemConta.findById(idItemConta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado."));

        // Registra que o balcão começou a preparar (Status "Em Preparação")
        item.setDataRecebimentoBar(LocalDate.now());
        item.setHoraRecebimentoBar(LocalTime.now());

        return iItemConta.save(item);
    }

    @Transactional
    public ItemConta registrarEntregaGarcom(Long idItemConta, Integer codigoGarcom) {
        ItemConta item = iItemConta.findById(idItemConta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado."));

        // Valida se o garçom que veio retirar o pedido existe e tem o cargo correto [cite: 157, 158]
        User garcom = iUsuario.findById(codigoGarcom)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código de garçom inválido."));

        if (!garcom.getTipo().equals(UserType.GARCOM)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "O código informado não pertence a um garçom.");
        }

        // Registra a saída final pelo balcão (Status "Entregue") [cite: 156, 160]
        item.setDataEntregaBar(LocalDate.now());
        item.setHoraEntregaBar(LocalTime.now());

        // Nota: O diagrama de classes persistentes não especificou um campo extra em Itens_da_Conta
        // para gravar o garçom de entrega [cite: 204], mas a validação acima garante a segurança exigida pelo fluxo[cite: 160].

        return iItemConta.save(item);
    }
}