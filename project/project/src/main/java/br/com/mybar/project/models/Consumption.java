package br.com.mybar.project.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "consumo") // Define a relationship between the consumption class and the consumption table
public class Consumption {

    @Id // define this field as a primary key
    @Column(name = "id") // Relates this attribute to the database column
    private int id; // auto generated

    @Column(name = "quantidade", nullable = false) // Relates this attribute to the database column
    private int quantity;

    @Column(name = "data", nullable = true, insertable = false, updatable = false)
    private LocalDateTime date;

    @OneToOne // Define a relationship one to one
    @JoinColumn(name = "num_item", nullable = false) // Relates this attribute to the database column
    private Item item;

    @ManyToOne // Define a relationship many to one
    @JoinColumn(name = "conta_id", nullable = false) // Relates this attribute to the database column
    private Account account;

    public int getQuantity() {
        return quantity;
    }

    public Account getAccount() {
        return account;
    }

    public int getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
