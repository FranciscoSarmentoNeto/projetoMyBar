package br.com.mybar.project.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.mybar.project.model.ItemType;
import br.com.mybar.project.model.DataTransferObject.ItemTypeDTO;
import br.com.mybar.project.repository.ItemTypeRepositoryInterface;
import br.com.mybar.project.repository.MenuItemRepositoryInterface;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemTypeService {
    private final ItemTypeRepositoryInterface tipoItemRepository;
    private final MenuItemRepositoryInterface itemCardapioRepository;

    public ItemTypeService(ItemTypeRepositoryInterface tipoItemRepository, MenuItemRepositoryInterface itemCardapioRepository) {
        this.tipoItemRepository = tipoItemRepository;
        this.itemCardapioRepository = itemCardapioRepository;
    }

    // Atende ao comando "Pesquisar" da tela, buscando por descrição
    public List<ItemType> pesquisar(String descricao) {
        List<ItemType> todosAtivos = tipoItemRepository.findByAtivoTrue();

        if (descricao != null && !descricao.trim().isEmpty()) {
            return todosAtivos.stream()
                    .filter(t -> t.getDescricao().toLowerCase().contains(descricao.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return todosAtivos;
    }

    public ItemType buscarPorCodigo(Integer codigo) {
        return tipoItemRepository.findById(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de Item não encontrado."));
    }

    @Transactional
    public ItemType salvar(ItemType tipoItem) {
        // Como o Persistable lida com a flag isNovo internamente,
        // o Spring Data JPA saberá fazer INSERT ou UPDATE corretamente.
        return tipoItemRepository.save(tipoItem);
    }

    @Transactional
    public ItemType editar(Integer id, ItemTypeDTO data){
        ItemType tipoItemExistente = tipoItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TipoItem não encontrado"));
        tipoItemExistente.setDescricao(data.descricao());
        tipoItemExistente.setCozinha(data.cozinha());
        tipoItemExistente.setGorjeta(data.gorjeta());
        return tipoItemRepository.save(tipoItemExistente);
    }

    @Transactional
    public void excluir(Integer codigo) {
        ItemType tipoItem = buscarPorCodigo(codigo);

        // para verificar se existem itens associados a este tipo.
        boolean possuiItensAssociados = verificarSePossuiItensNoCardapio(codigo);

        if (possuiItensAssociados) {
            // Se houver itens, apenas desativa impedindo a visualização
            tipoItem.setAtivo(false);
            tipoItemRepository.save(tipoItem);
        } else {
            // Se não houver, exclui definitivamente do banco
            tipoItemRepository.delete(tipoItem);
        }
    }

    private boolean verificarSePossuiItensNoCardapio(Integer codigo) {
        // Devolve true se o banco encontrar qualquer item atrelado a este tipo
        return itemCardapioRepository.existsByTipoItemCodigo(codigo);
    }
}

