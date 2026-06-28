package br.com.mybar.project.controllers;

import br.com.mybar.project.DAOs.ConsumptionInterface;
import br.com.mybar.project.DAOs.ItemInterface;
import br.com.mybar.project.request.ItemRequest.ItemMoreRevenueRequest;
import br.com.mybar.project.request.ItemRequest.ItemMoreSaleRequest;
import br.com.mybar.project.request.ItemRequest.ItemPostResquest;
import br.com.mybar.project.request.ItemRequest.ItemPutRequest;
import br.com.mybar.project.services.BarService.ValidationDataUpdateService;
import br.com.mybar.project.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class ItemController {

    @Autowired
    private ItemInterface DAO; // Automatically generates DAOs methods
    @Autowired
    private ConsumptionInterface consumptionDAO;
    @Autowired
    private ValidationDataUpdateService validationDataUpdateService;

    @GetMapping("/cardapio")
    public ResponseEntity<?> getItems() // Recover Cardapio on database
    {
        try{
            ArrayList<Item> items = (ArrayList<Item>) DAO.findAll();

            ArrayList<Item> tempItem = new ArrayList<>();

            for(Item item: items)
                if (item.getNumber_item() != 0)
                    tempItem.add(item);

            return ResponseEntity.ok(tempItem);
        }catch (Exception e)
        {
            return ResponseEntity.status(500).body("Erro: " + e);
        }
    }

    @GetMapping("/item/{num_item}")
    public Optional<Item> searchItem(@PathVariable int num_item) // Search Item by num_Item
    {
        // Item or Null
        return DAO.findById(num_item); // Return the item if it exists in the database
    }

    @PostMapping("/item")
    public ResponseEntity<?> postItem(@RequestBody ItemPostResquest itemResquest)// Save one new item
    {
        if(itemResquest.getNumber_item() == 0)
            return ResponseEntity.status(500).body("O ingresso não pode ser criado por este endpoint!");

        Item item = new Item();

        // Evita conflicts com o json
        item.setAvailable(itemResquest.isAvailable());
        item.setName(itemResquest.getName());
        item.setNumber_item(itemResquest.getNumber_item());
        item.setType(itemResquest.getType());
        item.setValue(itemResquest.getValue());

        return ResponseEntity.ok().body(DAO.save(item)); // Return new item
    }

    @PutMapping("/item")
    public ResponseEntity<?> updateItem(@RequestBody ItemPutRequest request) // Updates a customer information
    {
        try {
            if(!validationDataUpdateService.validationDataUpdate())
                return ResponseEntity.status(404).body("Regras de negocio nao podem ser alteradas enquanto tiver alguma mesa aberda");

            if (request.getNumber_item() == 0)
                return ResponseEntity.status(500).body("O ingresso não pode ser criado por este endpoint!");

            Item item = DAO.findById(request.getNumber_item()).orElse(null);

            if (!(item == null)) {
                if (request.getAvailable() != null)
                    item.setAvailable(request.getAvailable());

                if (request.getName() != null)
                    item.setName(request.getName());

                if (request.getType() != null)
                    item.setType(request.getType());

                if (request.getValue() != null)
                    item.setValue(request.getValue());
            } else {
                if (request.getValue() != null && request.getName() != null && request.getType() != null) {
                    item = new Item();

                    item.setNumber_item(request.getNumber_item());
                    item.setValue(request.getValue());
                    item.setType(request.getType());
                    item.setName(request.getName());
                    item.setAvailable(true);
                }
            }

            if (item == null)
                return ResponseEntity.status(404).body("Tentando criar um item com dados incompletos!");

            return ResponseEntity.ok(DAO.save(item)); // Return update item
        }catch (Exception e)
        {
            return ResponseEntity.status(500).body("Erro: " + e);
        }
    }

    @DeleteMapping("/cardapio")
    public ResponseEntity<?> deleteItems() // Delete Cardapio
    {
        try {
            if(!validationDataUpdateService.validationDataUpdate())
                return ResponseEntity.status(404).body("Regras de negocio nao podem ser alteradas enquanto tiver alguma mesa aberda");

            DAO.deleteAll();
            return ResponseEntity.ok().body("Cardapio deletado com sucesso");
        }catch (Exception e)
        {
            return ResponseEntity.status(500).body("Erro: " + e);
        }
    }

    @PutMapping("/item/{num_item}")
    public ResponseEntity<?> deleteItem(@PathVariable int num_item) // Delete item by num_item
    {
        try {
            if(!validationDataUpdateService.validationDataUpdate())
                return ResponseEntity.status(404).body("Regras de negocio nao podem ser alteradas enquanto tiver alguma mesa aberda");

            Item item = DAO.findById(num_item).orElse(null);

            if(item == null)
                return ResponseEntity.status(404).body("Item não existe!");

            if(!item.isAvailable())
                return ResponseEntity.status(404).body("Item já tá indisponivel");

            item.setAvailable(false);
            DAO.save(item);

            return ResponseEntity.ok().body("Item deletado");
        }catch (Exception e)
        {
            return ResponseEntity.status(500).body("Erro: " + e);
        }
    }
}