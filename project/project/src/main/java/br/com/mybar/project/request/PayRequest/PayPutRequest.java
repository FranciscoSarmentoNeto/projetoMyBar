package br.com.mybar.project.request.PayRequest;

public class PayPutRequest {
    private int pay_id;
    private Integer conta_id;
    private Double valor;
    private String autor;

    public String getAutor() {
        return autor;
    }

    public Double getValor() {
        return valor;
    }

    public int getPay_id() {
        return pay_id;
    }

    public Integer getConta_id() {
        return conta_id;
    }
}
