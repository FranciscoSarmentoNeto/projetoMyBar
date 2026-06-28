package br.com.mybar.project.services.TipService;

import br.com.mybar.project.DAOs.ConsumptionInterface;
import br.com.mybar.project.request.TipRequest.TipRequest;
import br.com.mybar.project.request.TipRequest.TipValuesRequest;
import br.com.mybar.project.models.Consumption;
import br.com.mybar.project.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class TipCalculationService {

    @Autowired
    private ConsumptionInterface consumptionDAO;

    public TipValuesRequest tipCalculation(int accountId) throws IOException {
        TipManagerService tipService = new TipManagerService();
        TipRequest tips = tipService.loadTipsPercents();
        ArrayList<Consumption> consumptions = consumptionDAO.findByAccountId(accountId);
        TipValuesRequest tipValuesRequest = new TipValuesRequest();

        double tipFull = 0.0;
        double tipDrink = 0.0;
        double tipFood = 0.0;

        for(Consumption consumption: consumptions)
        {
            Item item = consumption.getItem();
            switch (consumption.getItem().getType())
            {
                case 1:
                    tipFull += (tips.getTipPercentDrink() / 100) * (consumption.getQuantity() * item.getValue());
                    tipFood += (tips.getTipPercentDrink() / 100) * (consumption.getQuantity() * item.getValue());
                    break;
                case 2:
                    tipFull += (tips.getTipPercentFood() / 100) * (consumption.getQuantity() * item.getValue());
                    tipDrink += (tips.getTipPercentFood() / 100) * (consumption.getQuantity() * item.getValue());
                    break;
                default:
                    break;
            }
        }

        tipValuesRequest.setTipDrinkValue(tipDrink);
        tipValuesRequest.setTipFoodValue(tipFood);
        tipValuesRequest.setTipFullValue(tipFull);
        return tipValuesRequest;
    }
}
