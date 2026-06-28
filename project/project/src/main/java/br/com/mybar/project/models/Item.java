package br.com.mybar.project.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "item") // Defines a relationship between the Item class and the Item table
public class Item {

    @Id // Defines this field as a primary key
    @Column(name = "num_item") // Relates this attribute to the database column
    private int number_item;

    @Column(name = "tipo", nullable = false) // Relates this attribute to the database column
    private int type;

    @Column(name = "valor", nullable = false) // Relates this attribute to the database column
    private double value;

    @Column(name = "nome", length = 100, nullable = false) // Relate this attribute to the database column
    private String name;

    @Column(name = "disponivel", nullable = false)
    private boolean isAvailable;

    public double getValue() {
        return value;
    }

    public int getNumber_item() {
        return number_item;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setNumber_item(int number_item) {
        this.number_item = number_item;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isAvailable() {
        return this.isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}
