package com.haftu.stockservice.controller;

import com.haftu.stockservice.model.Stock;
import com.haftu.stockservice.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/check/{barcode}/{quantity}")
    public Boolean checkAvailability(@PathVariable String barcode,
                                     @PathVariable Integer quantity) {
        return stockService.checkAvailability(barcode, quantity);
    }

    @PutMapping("/update/{barcode}/{quantity}")
    public ResponseEntity<?> updateStock(@PathVariable String barcode,
                                         @PathVariable Integer quantity) {
        stockService.updateStock(barcode, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
        Stock savedStock = stockService.addStock(stock);
        return ResponseEntity.ok(savedStock);
    }

    @GetMapping("/{barcode}")
    public ResponseEntity<Stock> getStock(@PathVariable String barcode) {
        Stock stock = stockService.getStock(barcode);
        return ResponseEntity.ok(stock);
    }
}