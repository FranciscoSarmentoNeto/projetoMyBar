package br.com.mybar.project.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

import br.com.mybar.project.model.DefinedTypes.PaymentMethod;

@Entity
@Table(name = "pagamento")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false)
    private PaymentMethod formaPagamento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    private Account conta;
    public Payment(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PaymentMethod getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(PaymentMethod formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Account getConta() {
        return conta;
    }

    public void setConta(Account conta) {
        this.conta = conta;
    }
}
