package br.com.mybar.project.DAOs;
import br.com.mybar.project.models.Client;
import org.springframework.data.repository.CrudRepository;

// CrudRepository<Object, Primary key type>
// Abstract the requests with the database for this object
public interface ClientInterface extends CrudRepository<Client, String> {
}
