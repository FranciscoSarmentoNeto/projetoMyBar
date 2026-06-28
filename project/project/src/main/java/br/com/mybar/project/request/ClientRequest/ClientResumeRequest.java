package br.com.mybar.project.request.ClientRequest;

import br.com.mybar.project.models.Consumption;

import java.util.ArrayList;

public class ClientResumeRequest {

    private ArrayList<Consumption> consumptions;
    private double consumptionsValue;
    private double tipFull;
    private double accountValue;
    private double covert;
    private double tipDrink;
    private double tipFood;
    private int accountId;

    public void setAccountValue(double accountValue) {
        this.accountValue = accountValue;
    }

    public void setConsumptions(ArrayList<Consumption> consumptions) {
        this.consumptions = consumptions;
    }

    public void setConsumptionsValue(double consumptionsValue) {
        this.consumptionsValue = consumptionsValue;
    }

    public void setCovert(double covert) {
        this.covert = covert;
    }

    public void setTipFull(double tipFull) {
        this.tipFull = tipFull;
    }

    public ArrayList<Consumption> getConsumptions() {
        return consumptions;
    }

    public double getAccountValue() {
        return accountValue;
    }

    public double getConsumptionsValue() {
        return consumptionsValue;
    }

    public double getCovert() {
        return covert;
    }

    public double getTipFull() {
        return tipFull;
    }

    public double getTipDrink() {
        return tipDrink;
    }

    public double getTipFood() {
        return tipFood;
    }

    public void setTipDrink(double tipDrink) {
        this.tipDrink = tipDrink;
    }

    public void setTipFood(double tipFood) {
        this.tipFood = tipFood;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
