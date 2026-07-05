package br.com.mybar.project.service;

import br.com.mybar.project.model.ItemType;
import br.com.mybar.project.model.MenuItem;
import br.com.mybar.project.repository.MenuItemRepositoryInterface;
import br.com.mybar.project.repository.AccountItemRepositoryInterface; // Novo import
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.mybar.project.repository.ItemTypeRepositoryInterface;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemService {

    private final MenuItemRepositoryInterface itemCardapioRepository;
    private final AccountItemRepositoryInterface itemContaRepository; // Nova dependência
    private final ItemTypeRepositoryInterface tipoItemRepository;

    // Construtor atualizado com a injeção do ItemContaRepository
    public MenuItemService(MenuItemRepositoryInterface itemCardapioRepository, AccountItemRepositoryInterface itemContaRepository, ItemTypeRepositoryInterface tipoItemRepository) {
        this.itemCardapioRepository = itemCardapioRepository;
        this.itemContaRepository = itemContaRepository;
        this.tipoItemRepository = tipoItemRepository;

    }

    public List<MenuItem> pesquisar(String descricao) {
        List<MenuItem> todosAtivos = itemCardapioRepository.findByAtivoTrue();

        if (descricao != null && !descricao.trim().isEmpty()) {
            return todosAtivos.stream()
                    .filter(i -> i.getDescricao().toLowerCase().contains(descricao.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return todosAtivos;
    }

    public MenuItem buscarPorCodigo(Integer codigo) {
        return itemCardapioRepository.findById(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item de cardápio não encontrado."));
    }

    /*@Transactional
    public MenuItem salvar(MenuItem itemCardapio) {

        if (itemCardapio.getTipoItem() != null && itemCardapio.getTipoItem().getId() != null) {
            Integer tipoId = itemCardapio.getTipoItem().getId();

            ItemType tipoGerenciado = tipoItemRepository.findById(tipoId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de Item com o ID " + tipoId + " não existe."));

            itemCardapio.setTipoItem(tipoGerenciado);
        }

        return itemCardapioRepository.save(itemCardapio);
    }*/

    @Transactional
    public void excluir(Integer codigo) {
        MenuItem item = buscarPorCodigo(codigo);

        boolean possuiContasAssociadas = verificarSePossuiContasAssociadas(codigo);

        if (possuiContasAssociadas) {
            item.setAtivo(false);
            itemCardapioRepository.save(item);
        } else {
            itemCardapioRepository.delete(item);
        }
    }




    private boolean verificarSePossuiContasAssociadas(Integer codigo) {
        return itemContaRepository.existsByItemCardapioCodigo(codigo);
    }


@Transactional
public MenuItem salvar(MenuItem itemCardapio) {
    
    if (itemCardapio.getCodigo() != null && itemCardapioRepository.existsById(itemCardapio.getCodigo())) {
        
        MenuItem itemExistente = itemCardapioRepository.findById(itemCardapio.getCodigo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item de cardápio não encontrado."));

        itemExistente.setDescricao(itemCardapio.getDescricao());
        itemExistente.setValor(itemCardapio.getValor());
        itemExistente.setAtivo(itemCardapio.getAtivo());

        if (itemCardapio.getTipoItem() != null && itemCardapio.getTipoItem().getId() != null) {
            Integer tipoId = itemCardapio.getTipoItem().getId();
            ItemType tipoGerenciado = tipoItemRepository.findById(tipoId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de Item com o ID " + tipoId + " não existe."));
            itemExistente.setTipoItem(tipoGerenciado);
        }

        return itemCardapioRepository.save(itemExistente);
    }

    if (itemCardapio.getTipoItem() != null && itemCardapio.getTipoItem().getId() != null) {
        Integer tipoId = itemCardapio.getTipoItem().getId();
        ItemType tipoGerenciado = tipoItemRepository.findById(tipoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de Item com o ID " + tipoId + " não existe."));
        itemCardapio.setTipoItem(tipoGerenciado);
    }

    return itemCardapioRepository.save(itemCardapio);
}







}