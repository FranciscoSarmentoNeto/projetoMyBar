package br.com.mybar.project.models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cliente") // Defines a relationship between the client class and the client table
public class Client {

    @Id // Defines this field as a primary key
    @Column(name = "cpf") // Relates this attribute to the database column
    private String cpf;

    @Column(name = "nome", length = 100, nullable = false) // Relates this attribute to the database column
    private String name;

    public void setName(String name) {
        this.name = name;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getName() {
        return name;
    }
    public String getCpf() {
        return cpf;
    }
}
