package com.haftu.order.controller;

import com.haftu.order.model.Order;
import com.haftu.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order orderDetails) {
        Order createdOrderDetails = orderService.createOrder(orderDetails);
        return ResponseEntity.ok(createdOrderDetails);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable UUID id) {
        Order orderDetails = orderService.getOrder(id);
        return ResponseEntity.ok(orderDetails);
    }
}