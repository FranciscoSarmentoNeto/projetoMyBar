package br.com.mybar.project.request.ConsumptionRequest;

public class ConsumptionIntervalRequest {
    private String nameItem;
    private int quantity;
    private String date;
    private String client_cpf;

    public void setDate(String date) {
        this.date = date;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setClient_cpf(String client_cpf) {
        this.client_cpf = client_cpf;
    }

    public String getClient_cpf() {
        return client_cpf;
    }
}
