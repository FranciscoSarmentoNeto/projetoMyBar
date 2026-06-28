package br.com.mybar.project.DAOs;

import br.com.mybar.project.models.Pay;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface PayInterface extends CrudRepository<Pay, Integer> {

    // Finds payments by Account id
    public ArrayList<Pay> findByAccountId(int id);

    // Finds payments by author of payment
    public ArrayList<Pay> findByAuthor(String nome);

    @Query("SELECT p FROM Pay p WHERE p.date BETWEEN :start AND :end") // Query criada de forma personalizada
    public ArrayList<Pay> findAllByDateBetween(LocalDateTime start, LocalDateTime end);
}
