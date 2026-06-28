package br.com.mybar.project.request.ItemRequest;

public class ItemMoreSaleRequest {
    private Number item_id;
    private String name;
    private Number quantity;

    public String getName() {
        return name;
    }

    public Number getItem_id() {
        return item_id;
    }

    public Number getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItem_id(Number item_id) {
        this.item_id = item_id;
    }

    public void setQuantity(Number quantity) {
        this.quantity = quantity;
    }
}