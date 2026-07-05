package br.com.mybar.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.mybar.project.model.MenuItem;

import java.util.List;

public interface MenuItemRepositoryInterface extends JpaRepository<MenuItem, Integer> {
    List<MenuItem> findByAtivoTrue();

    boolean existsByTipoItemCodigo(Integer codigoTipoItem);
}
