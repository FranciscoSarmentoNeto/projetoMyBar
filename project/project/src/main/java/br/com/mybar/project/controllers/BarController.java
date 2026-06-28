package br.com.mybar.project.controllers;

import br.com.mybar.project.DAOs.ItemInterface;
import br.com.mybar.project.request.BarResumeRequest;
import br.com.mybar.project.request.ItemRequest.ItemMoreRevenueRequest;
import br.com.mybar.project.request.ItemRequest.ItemMoreSaleRequest;
import br.com.mybar.project.request.TipRequest.TipRequest;
import br.com.mybar.project.services.BarService.BarResumeService;
import br.com.mybar.project.services.BarService.ValidationDataUpdateService;
import br.com.mybar.project.services.ItemResumeService;
import br.com.mybar.project.services.TipService.TipManagerService;
import br.com.mybar.project.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class BarController {

    @Autowired
    private BarResumeService barResumeService;
    @Autowired
    private TipManagerService tipService;
    @Autowired
    private ItemInterface itemDAO;
    @Autowired
    private ItemResumeService itemResumeService;
    @Autowired
    private ValidationDataUpdateService validationDataUpdateService;

    // Resumo sobre o bar
    @GetMapping("bar/resume")
    public ResponseEntity<BarResumeRequest> getBarResume(@RequestParam String startDate, @RequestParam String endDate)
    {
        return ResponseEntity.ok().body(barResumeService.getBarResume(startDate, endDate));
    }

    @GetMapping("bar/tip")
    public ResponseEntity<TipRequest> getTips() throws IOException {
        return ResponseEntity.ok(tipService.loadTipsPercents());
    }

    @PostMapping("bar/tip")
    public ResponseEntity<String> upDateTips(@RequestBody TipRequest request) throws  IOException {

        try {
            TipRequest tips = tipService.loadTipsPercents();

            if (!validationDataUpdateService.validationDataUpdate())
                return ResponseEntity.status(404).body("Regras de negocio nao podem ser alteradas enquanto tiver alguma mesa aberda");

            if (request.getTipPercentDrink() != null)
                tips.setTipPercentDrink(request.getTipPercentDrink());

            if (request.getTipPercentFood() != null)
                tips.setTipPercentFood(request.getTipPercentFood());

            tipService.saveTips(tips);

            return ResponseEntity.ok(String.format("Porcetagem de gorjeta atualizada: Comida - %.2f%%, Bebida - %.2f%%", tips.getTipPercentFood(), tips.getTipPercentDrink()));
        }catch (Exception e)
        {
            return ResponseEntity.status(500).body("Erro: " + e);
        }
    }

    @GetMapping("/bar/item/more/sale")
    public ResponseEntity<?> getMoreSale()
    {
        ArrayList<ItemMoreSaleRequest> report = itemResumeService.getMoreSale();

        return ResponseEntity.ok().body(report);
    }

    @GetMapping("/bar/item/more/revenue")
    public ResponseEntity<?> getItemRevenueReport() {
        // Monta a resposta
        ArrayList<ItemMoreRevenueRequest> report = itemResumeService.getItemRevenueReport();

        return ResponseEntity.ok().body(report);
    }

    // Get para o covert
    @GetMapping("/bar/covert")
    public Item getCovert()
    {
        return itemDAO.findById(0).orElse(null);
    }

    // Cria o covert como item
    @PostMapping("/bar/covert/{valueCovert}")
    public ResponseEntity<?> createdCovert(@PathVariable double valueCovert)
    {
        try {
            if(!validationDataUpdateService.validationDataUpdate())
                return ResponseEntity.status(404).body("Regras de negocio nao podem ser alteradas enquanto tiver alguma mesa aberda");

            Item covert = itemDAO.findById(0).orElse(null);

            if (covert == null) {
                covert = new Item();

                covert.setValue(valueCovert);
                covert.setType(0);
                covert.setName("Ingresso");
                covert.setNumber_item(0);
                covert.setAvailable(true);

                return ResponseEntity.ok().body(itemDAO.save(covert));
            }

            return ResponseEntity.ok().body(covert);
        }catch (Exception e)
        {
            return ResponseEntity.status(500).body("Erro: " + e);
        }
    }

    // Cria o covert como item
    @PutMapping("/bar/covert/{valueCovert}")
    public ResponseEntity<?> updateCovert(@PathVariable double valueCovert) {
        try {
            if(!validationDataUpdateService.validationDataUpdate())
                return ResponseEntity.status(404).body("Regras de negocio nao podem ser alteradas enquanto tiver alguma mesa aberda");

            Item covert = itemDAO.findById(0).orElse(null);

            if (covert == null) {
                covert = new Item();
                covert.setValue(valueCovert);
                covert.setType(0);
                covert.setName("Ingresso");
                covert.setNumber_item(0);
                covert.setAvailable(true);
            } else
                covert.setValue(valueCovert);

            return ResponseEntity.ok().body(itemDAO.save(covert));
        }catch (Exception e)
        {
            return ResponseEntity.status(500).body("Erro: " + e);
        }
    }
}