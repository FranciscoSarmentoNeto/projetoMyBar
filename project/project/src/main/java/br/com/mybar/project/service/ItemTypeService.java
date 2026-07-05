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

        boolean possuiItensAssociados = verificarSePossuiItensNoCardapio(codigo);

        if (possuiItensAssociados) {
            tipoItem.setAtivo(false);
            tipoItemRepository.save(tipoItem);
        } else {
            tipoItemRepository.delete(tipoItem);
        }
    }

    private boolean verificarSePossuiItensNoCardapio(Integer codigo) {
        return itemCardapioRepository.existsByTipoItemCodigo(codigo);
    }
}

