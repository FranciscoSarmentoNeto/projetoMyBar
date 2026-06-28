package br.com.mybar.project.request.TipRequest;

public class TipValuesRequest {

    private double tipDrinkValue;
    private double tipFoodValue;
    private double tipFullValue;

    public double getTipDrinkValue() {
        return tipDrinkValue;
    }

    public double getTipFoodValue() {
        return tipFoodValue;
    }

    public void setTipDrinkValue(double tipDrinkValue) {
        this.tipDrinkValue = tipDrinkValue;
    }

    public void setTipFoodValue(double tipFoodValue) {
        this.tipFoodValue = tipFoodValue;
    }

    public double getTipFullValue() {
        return tipFullValue;
    }

    public void setTipFullValue(double tipFullValue) {
        this.tipFullValue = tipFullValue;
    }
}
