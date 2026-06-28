package br.com.mybar.project.DAOs;

import br.com.mybar.project.models.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

// CrudRepository<Object, primary ley type>
// Abstract the requests with the database for this object
public interface ItemInterface extends CrudRepository<Item, Integer> {
    @Query("""
        SELECT c.item.id, c.item.name, SUM(c.quantity)
        FROM Consumption c
        GROUP BY c.item.id, c.item.name
        ORDER BY SUM(c.quantity) DESC
    """)
    ArrayList<Object[]> findMoreSale(Pageable pageable);

    @Query("""
        SELECT c.item.id,c.item.name, SUM(c.quantity * c.item.value)
        FROM Consumption c
        GROUP BY c.item.id, c.item.name
        ORDER BY SUM(c.quantity * c.item.value) DESC
    """)
    ArrayList<Object[]> findMoreRevenue(
            Pageable pageable
    );

}
