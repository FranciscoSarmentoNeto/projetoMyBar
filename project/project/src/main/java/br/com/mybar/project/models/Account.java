package br.com.mybar.project.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "conta") // Define a relationship between the account class and the account table
public class Account {

    @Id // Defines this field as a primary key
    @Column(name = "id", nullable = false) // Relates this attribute to the database column
    private int id; // auto generated

    @ManyToOne // Define a relationship many to one
    @JoinColumn(name = "cliente_cpf", nullable = false) // Relates this attribute to the database column
    private Client client;

    @Column(name = "aberta", nullable = false)
    private boolean isOpen;

    @Column(name = "pessoas", nullable = false)
    private int peoples;

    @Column(name = "data_fecha", nullable = true)
    private LocalDateTime date_close;

    @Column(name = "gor_bebida", nullable = true)
    private Double tipDrink;

    @Column(name = "gor_comida", nullable = true)
    private Double tipFood;

    @Column(name = "conta_value", nullable = true)
    private Double value;

    public Client getClient() {
        return client;
    }

    public int getAccountId() {
        return id;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setAccountId(int idConta) {
        this.id = idConta;
    }

    public int getId() {
        return id;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPeoples() {
        return peoples;
    }

    public void setPeoples(int peoples) {
        this.peoples = peoples;
    }

    public void setOpen(boolean open) {
        System.out.printf("Mesa aberta: %b", open);
        this.isOpen = open;
    }

    public LocalDateTime getDate_close() {
        return date_close;
    }

    public void setDate_close(LocalDateTime date_close) {
        this.date_close = date_close;
    }

    public void setTipDrink(Double tipDrink) {
        this.tipDrink = tipDrink;
    }

    public void setTipFood(Double tipFood) {
        this.tipFood = tipFood;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getTipDrink() {
        return tipDrink;
    }

    public Double getValue() {
        return value;
    }

    public Double getTipFood() {
        return tipFood;
    }
}
