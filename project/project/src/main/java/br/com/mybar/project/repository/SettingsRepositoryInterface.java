package br.com.mybar.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.mybar.project.model.Config;


public interface SettingsRepositoryInterface extends JpaRepository<Config, Integer> {
}
