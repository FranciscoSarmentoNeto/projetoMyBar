package br.com.mybar.project.services;

import br.com.mybar.project.DAOs.ItemInterface;
import br.com.mybar.project.request.ItemRequest.ItemMoreRevenueRequest;
import br.com.mybar.project.request.ItemRequest.ItemMoreSaleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ItemResumeService {
    @Autowired
    private ItemInterface itemDAO;

    public ArrayList<ItemMoreSaleRequest> getMoreSale()
    {
        ArrayList<Object[]> top = itemDAO.findMoreSale(PageRequest.of(0, 10));

        ArrayList<ItemMoreSaleRequest> report = new ArrayList<>();

        for(Object[] t: top)
        {
            if((int) t[0] != 0) {
                ItemMoreSaleRequest item = new ItemMoreSaleRequest();

                item.setItem_id((Number) t[0]);
                item.setName((String) t[1]);
                item.setQuantity((Number) t[2]);

                report.add(item);
            }
        }

        return report;
    }

    public ArrayList<ItemMoreRevenueRequest> getItemRevenueReport() {
        // Chama o top 10 de faturamento
        ArrayList<Object[]> result = itemDAO.findMoreRevenue(
                PageRequest.of(0, 10) // TOP 10
        );

        // Monta a resposta
        ArrayList<ItemMoreRevenueRequest> report = new ArrayList<>();

        for (Object[] row : result) {
            if((int) row[0] != 0) {

                ItemMoreRevenueRequest request = new ItemMoreRevenueRequest();

                request.setItem_id(((Number) row[0]).intValue());
                request.setName((String) row[1]);
                request.setRevenue(((Number) row[2]).doubleValue());

                report.add(request);
            }
        }

        return report;
    }
}