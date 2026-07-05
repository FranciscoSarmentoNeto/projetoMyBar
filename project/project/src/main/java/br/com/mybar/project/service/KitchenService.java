package br.com.mybar.project.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import br.com.mybar.project.model.AccountItem;
import br.com.mybar.project.repository.AccountItemRepositoryInterface; // Assumindo que você criou a interface com este padrão

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class KitchenService {

    @Autowired
    private AccountItemRepositoryInterface itemContaRepository;

    @Transactional
    public AccountItem receberPedido(Long idItemConta) {
        AccountItem item = itemContaRepository.findById(idItemConta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado."));

        if (!item.getItemCardapio().getTipoItem().getCozinha()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este item não é de preparo da cozinha.");
        }

        item.setDataRecebimentoCozinha(LocalDate.now());
        item.setHoraRecebimentoCozinha(LocalTime.now());

        return itemContaRepository.save(item);
    }

    @Transactional
    public AccountItem entregarPedido(Long idItemConta) {
        AccountItem item = itemContaRepository.findById(idItemConta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado."));

        if (item.getDataRecebimentoCozinha() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O pedido precisa ser recebido antes de ser entregue.");
        }

        item.setDataEntregaCozinha(LocalDate.now());
        item.setHoraEntregaCozinha(LocalTime.now());

        return itemContaRepository.save(item);
    }
}