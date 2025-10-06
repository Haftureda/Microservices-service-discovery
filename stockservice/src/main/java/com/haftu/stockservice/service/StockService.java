package com.haftu.stockservice.service;

import com.haftu.stockservice.model.Stock;
import com.haftu.stockservice.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public Boolean checkAvailability(String barcode, Integer requestedQuantity) {
        Stock stock = stockRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return stock.getQuantity() >= requestedQuantity;
    }

    @Transactional
    public void updateStock(String barcode, Integer soldQuantity) {
        // Use pessimistic locking to prevent concurrent updates
        Stock stock = stockRepository.findByBarcodeWithLock(barcode)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (stock.getQuantity() < soldQuantity) {
            throw new RuntimeException("Insufficient stock");
        }

        stock.setQuantity(stock.getQuantity() - soldQuantity);
        stock.setLastUpdated(LocalDateTime.now());
        stockRepository.save(stock);
    }

    @Transactional
    public Stock addStock(Stock stock) {
        // Check if product already exists
        Optional<Stock> existingStock = stockRepository.findByBarcode(stock.getBarcode());

        if (existingStock.isPresent()) {
            // Update existing stock
            Stock existing = existingStock.get();
            existing.setQuantity(existing.getQuantity() + stock.getQuantity());
            existing.setPrice(stock.getPrice());
            existing.setLastUpdated(LocalDateTime.now());
            return stockRepository.save(existing);
        } else {
            // Create new stock entry
            stock.setLastUpdated(LocalDateTime.now());
            return stockRepository.save(stock);
        }
    }

    public Stock getStock(String barcode) {
        return stockRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
