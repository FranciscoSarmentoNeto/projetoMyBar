package br.com.mybar.project.request.ItemRequest;

public class ItemMoreRevenueRequest {
    private int item_id;
    private String name;
    private double revenue;

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRevenue() {
        return revenue;
    }

    public int getItem_id() {
        return item_id;
    }

    public String getName() {
        return name;
    }
}