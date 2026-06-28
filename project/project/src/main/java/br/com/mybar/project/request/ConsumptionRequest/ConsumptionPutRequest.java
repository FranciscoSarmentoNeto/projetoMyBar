package br.com.mybar.project.request.ConsumptionRequest;

public class ConsumptionPutRequest {
    private int consumption_id;
    private Integer quantidade;
    private Integer num_item;
    private Integer conta_id;

    public int getConsumption_id() {
        return consumption_id;
    }

    public Integer getConta_id() {
        return conta_id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Integer getNum_item() {
        return num_item;
    }
}
