package br.com.mybar.project.request.PayRequest;

// Body for request of post Pay
public class PayPostRequest {
    private int conta_id;
    private Double valor;
    private String autor;

    public Double getValor() {
        return valor;
    }

    public String getAutor() {
        return autor;
    }

    public int getConta_id() {
        return conta_id;
    }
}
