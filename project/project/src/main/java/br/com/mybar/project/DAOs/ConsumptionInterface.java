package br.com.mybar.project.DAOs;

import br.com.mybar.project.models.Consumption;
import br.com.mybar.project.models.Pay;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

// CrudRepository<Object, primary key type>
// Abstract the requests with the database for this object
public interface ConsumptionInterface extends CrudRepository<Consumption, Integer> {

    // Generates the query by signing the method, this signature being on the way to a "where"
    // FROM Consumo consumo JOIN Conta conta ON Consumo.conta_id = conta.id
    // Finds all the consumptions of the account that has the id that was passed as parameter
    ArrayList<Consumption> findByAccountId(int id);

    // Find consumptions by client name
    ArrayList<Consumption> findByAccountClientName(String nome);

    @Query("""
       SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
       FROM Consumption c
       WHERE c.account.id = :accountId
         AND c.item.number_item = 0
       """)
    boolean existsCouvertByAccountId(int accountId);

    @Query("SELECT c FROM Consumption c WHERE c.date BETWEEN :start AND :end") // Query criada de forma personalizada
    public ArrayList<Consumption> findAllByDateBetween(LocalDateTime start, LocalDateTime end);
}
