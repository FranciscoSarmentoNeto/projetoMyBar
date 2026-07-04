package br.com.mybar.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.mybar.project.model.MenuItem;

import java.util.List;

public interface MenuItemRepositoryInterface extends JpaRepository<MenuItem, Integer> {
    // Traz apenas os itens que não sofreram "soft delete"
    List<MenuItem> findByAtivoTrue();

    // Verifica se existe algum Item de Cardápio vinculado a um determinado código de Tipo
    boolean existsByTipoItemCodigo(Integer codigoTipoItem);
}
