package br.com.mybar.project.model.Actors;

import br.com.mybar.project.model.DefinedTypes.Gender;
import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "cpf")
    private String cpf;
    @Column(name = "nome")
    private String nome;
    @Column(name = "telefone")
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private Gender sexo;
    public Client(){

    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Gender getSexo() {
        return sexo;
    }

    public void setSexo(Gender sexo) {
        this.sexo = sexo;
    }
}
