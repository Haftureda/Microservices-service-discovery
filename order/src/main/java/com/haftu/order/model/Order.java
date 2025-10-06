package com.haftu.order.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders-table")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    private String barcode;
    private Double price;
    private Integer quantity;
    private LocalDateTime transactionDate;
    private String status; // PENDING, CONFIRMED, CANCELLED, COMPLETED
    private String paymentId;

}