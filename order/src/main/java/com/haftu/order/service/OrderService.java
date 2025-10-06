package com.haftu.order.service;

import com.haftu.order.dto.PaymentResponse;
import com.haftu.order.dto.PaymentRequest;
import com.haftu.order.model.Order;
import com.haftu.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Order createOrder(Order order) {
        order.setTransactionDate(LocalDateTime.now());
        order.setStatus("PENDING");

        // Check stock availability
        Boolean available = restTemplate.getForObject(
                "http://stock-service/stocks/check/{barcode}/{quantity}",
                Boolean.class,
                order.getBarcode(),
                order.getQuantity()
        );

        if (Boolean.TRUE.equals(available)) {
            Order savedOrder = orderRepository.save(order);

            // Initiate payment
            PaymentRequest paymentRequest = new PaymentRequest(
                    savedOrder.getId(),
                    savedOrder.getPrice() * savedOrder.getQuantity()
            );

            PaymentResponse paymentResponse = restTemplate.postForObject(
                    "http://payment-service/payments/process",
                    paymentRequest,
                    PaymentResponse.class
            );

            if ("SUCCESS".equals(paymentResponse.getStatus())) {
                savedOrder.setStatus("CONFIRMED");
                savedOrder.setPaymentId(paymentResponse.getPaymentId());
                orderRepository.save(savedOrder);

                // Update stock
                restTemplate.put(
                        "http://stock-service/stocks/update/{barcode}/{quantity}",
                        null,
                        order.getBarcode(),
                        order.getQuantity()
                );
            } else {
                savedOrder.setStatus("CANCELLED");
                orderRepository.save(savedOrder);
            }

            return savedOrder;
        } else {
            throw new RuntimeException("Insufficient stock");
        }
    }

    public Order getOrder(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}