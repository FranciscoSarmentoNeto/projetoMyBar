package br.com.mybar.project.request.ItemRequest;

public class ItemPutRequest {
    private int number_item;
    private Integer type;
    private Double value;
    private String name;
    private Boolean available;

    public int getNumber_item() {
        return number_item;
    }

    public Boolean getAvailable() {
        return available;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    public Integer getType() {
        return type;
    }
}
