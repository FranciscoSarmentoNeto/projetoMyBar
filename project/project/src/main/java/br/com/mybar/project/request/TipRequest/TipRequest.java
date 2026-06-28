package br.com.mybar.project.request.TipRequest;

public class TipRequest {
    private Double tipPercentDrink;
    private Double tipPercentFood;

    public void setTipPercentDrink(Double tipPercentDrink) {
        this.tipPercentDrink = tipPercentDrink;
    }

    public void setTipPercentFood(Double tipPercentFood) {
        this.tipPercentFood = tipPercentFood;
    }

    public Double getTipPercentDrink() {
        return tipPercentDrink;
    }

    public Double getTipPercentFood() {
        return tipPercentFood;
    }
}
