package br.com.mybar.project.services.AccountService;

import br.com.mybar.project.DAOs.ConsumptionInterface;
import br.com.mybar.project.request.TipRequest.TipRequest;
import br.com.mybar.project.services.TipService.TipManagerService;
import br.com.mybar.project.models.Consumption;
import br.com.mybar.project.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class AccountCalculationConsumptionsService {

    @Autowired
    private ConsumptionInterface consumptionDAO;

    public Double accountCalculationConsumptions(int accountId) throws IOException {
        TipManagerService tipService = new TipManagerService();
        TipRequest tips = tipService.loadTipsPercents();
        ArrayList<Consumption> consumptions = consumptionDAO.findByAccountId(accountId);

        double accountValue = 0.0;

        for(Consumption consumption: consumptions)
        {
            Item item = consumption.getItem();
            switch (consumption.getItem().getType())
            {
                case 2:
                    accountValue += consumption.getQuantity() * item.getValue();
                    accountValue += (tips.getTipPercentDrink() / 100) * (consumption.getQuantity() * item.getValue());
                    break;
                case 3:
                    accountValue += consumption.getQuantity() * item.getValue();
                    accountValue += (tips.getTipPercentFood() / 100) * (consumption.getQuantity() * item.getValue());
                    break;
                default:
                    accountValue += consumption.getQuantity() * item.getValue();
                    break;
            }
        }

        return accountValue;
    }
}
