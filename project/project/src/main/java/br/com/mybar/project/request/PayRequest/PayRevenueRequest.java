package br.com.mybar.project.request.PayRequest;

public class PayRevenueRequest {

    private double revenue;
    private int amountPayments;


    public void revenueIncrement(double value) {
        revenue += value;
    }

    public void setAmountPayments(int amountPayments) {
        this.amountPayments = amountPayments;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getRevenue() {
        return revenue;
    }

    public int getAmountPayments() {
        return amountPayments;
    }
}
