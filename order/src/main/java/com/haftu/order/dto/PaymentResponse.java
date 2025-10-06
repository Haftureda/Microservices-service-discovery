package com.haftu.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentResponse {
    private String paymentId;
    private String status;
    private String transactionId;
    // constructors, getters, setters
}