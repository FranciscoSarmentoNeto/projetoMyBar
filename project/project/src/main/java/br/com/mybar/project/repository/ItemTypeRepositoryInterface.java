package br.com.mybar.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.mybar.project.model.ItemType;

import java.util.List;

public interface ItemTypeRepositoryInterface extends JpaRepository<ItemType, Integer> {
    List<ItemType> findByAtivoTrue();
}
