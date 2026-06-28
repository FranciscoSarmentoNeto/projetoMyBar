package br.com.mybar.project.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
public class Pay {

    @Id
    @Column(name = "id")
    private int id; // Id is Auto Increment

    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = true)
    private Account account;

    @Column(name = "valor", nullable = false)
    private Double value;

    @Column(name = "autor", length = 100, nullable = true)
    private String author;

    // insertable = false, indicates that the field should not be filled, as it will be filled in the database
    // updatable = false, indicates that the field should not be update
    @Column(name = "data", nullable = false, insertable = false, updatable = false)
    private LocalDateTime date; // date is auto generate

    public Account getAccount() {
        return account;
    }

    public int getId() {
        return id;
    }

    public Double getValue() {
        return value;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
