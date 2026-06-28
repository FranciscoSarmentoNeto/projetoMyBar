package br.com.mybar.project.request;

import br.com.mybar.project.request.ItemRequest.ItemMoreRevenueRequest;
import br.com.mybar.project.request.ItemRequest.ItemMoreSaleRequest;
import br.com.mybar.project.request.PayRequest.PayRevenueRequest;
import br.com.mybar.project.models.Item;

import java.util.ArrayList;

public class BarResumeRequest {
    private ArrayList<ItemMoreSaleRequest> itemsMoreSale;
    private ArrayList<ItemMoreRevenueRequest> itemsMoreRevenue;
    private PayRevenueRequest intervalRevenue;

    public PayRevenueRequest getIntervalRevenue() {
        return intervalRevenue;
    }

    public ArrayList<ItemMoreRevenueRequest> getItemsMoreRevenue() {
        return itemsMoreRevenue;
    }

    public ArrayList<ItemMoreSaleRequest> getItemsMoreSale() {
        return itemsMoreSale;
    }

    public void setItemsMoreSale(ArrayList<ItemMoreSaleRequest> itemsMoreSale) {
        this.itemsMoreSale = itemsMoreSale;
    }

    public void setItemsMoreRevenue(ArrayList<ItemMoreRevenueRequest> itemsMoreRevenue) {
        this.itemsMoreRevenue = itemsMoreRevenue;
    }

    public void setIntervalRevenue(PayRevenueRequest intervalRevenue) {
        this.intervalRevenue = intervalRevenue;
    }
}
