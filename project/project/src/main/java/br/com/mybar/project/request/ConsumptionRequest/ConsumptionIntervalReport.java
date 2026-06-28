package br.com.mybar.project.request.ConsumptionRequest;

import java.util.ArrayList;

public class ConsumptionIntervalReport {
    ArrayList<ConsumptionIntervalRequest> consumptions;
    double revenue;

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public void setConsumptions(ArrayList<ConsumptionIntervalRequest> consumptions) {
        this.consumptions = consumptions;
    }

    public double getRevenue() {
        return revenue;
    }

    public ArrayList<ConsumptionIntervalRequest> getConsumptions() {
        return consumptions;
    }
}
