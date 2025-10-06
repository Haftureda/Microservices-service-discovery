package com.haftu.apigateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallbacks")
public class FallbackController {

    @GetMapping("/orders")
    public ResponseEntity<Map<String, Object>> orderServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", 503);
        response.put("error", "Service Unavailable");
        response.put("message", "Order service is temporarily unavailable. Please try again later.");
        response.put("path", "/api/orders");

        return ResponseEntity.status(503).body(response);
    }

    @GetMapping("/stocks")
    public ResponseEntity<Map<String, Object>> stockServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", 503);
        response.put("error", "Service Unavailable");
        response.put("message", "Stock service is temporarily unavailable. Please try again later.");
        response.put("path", "/api/stock");

        return ResponseEntity.status(503).body(response);
    }

    @GetMapping("/payments")
    public ResponseEntity<Map<String, Object>> paymentServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", 503);
        response.put("error", "Service Unavailable");
        response.put("message", "Payment service is temporarily unavailable. Please try again later.");
        response.put("path", "/api/payments");

        return ResponseEntity.status(503).body(response);
    }
}