package br.com.mybar.project.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "lancamento_item")
public class ItemEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    private Account conta;

    @ManyToOne
    @JoinColumn(name = "item_cardapio_codigo", nullable = false)
    private MenuItem itemCardapio;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    public ItemEntry() {}

    public BigDecimal getValorTotal() {
        return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Account getConta() { return conta; }
    public void setConta(Account conta) { this.conta = conta; }

    public MenuItem getItemCardapio() { return itemCardapio; }
    public void setItemCardapio(MenuItem itemCardapio) { this.itemCardapio = itemCardapio; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
}