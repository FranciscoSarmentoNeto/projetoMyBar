package br.com.mybar.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.mybar.project.model.Actors.Client;

import java.util.Optional;

@Repository
public interface CustomerRepositoryInterface extends JpaRepository<Client, Long> {
    Optional<Client> findByCpf(String cpf);
}
