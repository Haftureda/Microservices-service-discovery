package com.haftu.stockservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Add auto-generated ID

    private String barcode;
    private String productName;
    private Double price;
    private Integer quantity;
    private LocalDateTime lastUpdated;

    // Add version for optimistic locking
    @Version
    private Integer version;

    // constructors, getters, setters
}