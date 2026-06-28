package br.com.mybar.project.services.BarService;

import br.com.mybar.project.request.BarResumeRequest;
import br.com.mybar.project.services.ItemResumeService;
import br.com.mybar.project.services.PayService.PayIntervalResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BarResumeService {

    @Autowired
    private PayIntervalResumeService payIntervalResumeService;
    @Autowired
    private ItemResumeService itemResumeService;

    public BarResumeRequest getBarResume(String startDate, String endDate)
    {
        BarResumeRequest request = new BarResumeRequest();


        request.setIntervalRevenue(payIntervalResumeService.getRevenueInterval(startDate, endDate));
        request.setItemsMoreSale(itemResumeService.getMoreSale());
        request.setItemsMoreRevenue(itemResumeService.getItemRevenueReport());

        return request;
    }

}
