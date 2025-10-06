package com.haftu.paymentsservice.service;

import com.haftu.paymentsservice.dto.PaymentRequest;
import com.haftu.paymentsservice.dto.PaymentResponse;
import com.haftu.paymentsservice.model.Payment;
import com.haftu.paymentsservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentResponse processPayment(PaymentRequest request) {
        // Check if payment already exists for this order
        Optional<Payment> existingPayment = paymentRepository.findByOrderId(request.getOrderId());
        if (existingPayment.isPresent()) {
            Payment payment = existingPayment.get();
            return new PaymentResponse(
                    payment.getPaymentId(),
                    payment.getStatus(),
                    payment.getTransactionId()
            );
        }

        // Create new payment
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(LocalDateTime.now());

        // Simulate payment processing (80% success rate)
        boolean success = Math.random() > 0.2;

        if (success) {
            payment.setStatus("SUCCESS");
            payment.setTransactionId("TXN" + System.currentTimeMillis());
        } else {
            payment.setStatus("FAILED");
            payment.setTransactionId(null);
        }

        paymentRepository.save(payment);

        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getStatus(),
                payment.getTransactionId()
        );
    }

    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));
    }

    public Payment getPaymentByOrderId(UUID orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order ID: " + orderId));
    }
}
