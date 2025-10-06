package com.haftu.paymentsservice.repository;

import com.haftu.paymentsservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByOrderId(UUID orderId);
    Optional<Payment> findByTransactionId(String transactionId);
}