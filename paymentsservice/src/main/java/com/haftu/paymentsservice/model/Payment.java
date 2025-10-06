package com.haftu.paymentsservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
public class Payment {
    @Id
    private String paymentId;

    private UUID orderId;
    private Double amount;
    private LocalDateTime paymentDate;
    private String status; // PENDING, SUCCESS, FAILED
    private String transactionId;

}