package com.haftu.stockservice.repository;

import com.haftu.stockservice.model.Stock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, String> {
    Optional<Stock> findByBarcode(String barcode);

    // For pessimistic locking
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Stock s WHERE s.barcode = :barcode")
    Optional<Stock> findByBarcodeWithLock(@Param("barcode") String barcode);

    // Alternative: Use native query for update
    @Modifying
    @Query("UPDATE Stock s SET s.quantity = s.quantity - :quantity, s.lastUpdated = CURRENT_TIMESTAMP WHERE s.barcode = :barcode AND s.quantity >= :quantity")
    int decrementStock(@Param("barcode") String barcode, @Param("quantity") Integer quantity);
}
