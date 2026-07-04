package br.com.mybar.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.mybar.project.model.Actors.User;

import java.util.Optional;


public interface UserRepositoryInterface extends JpaRepository<User, Integer> {
    UserDetails findByEmail(String email);
    Optional<User> findByCodigo(int codigo);
}
