package com.haftu.order.repository;

import com.haftu.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByBarcode(String barcode);
    List<Order> findByStatus(String status);
}