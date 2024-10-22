package com.ua.client_accounting.order.controller;

import com.ua.client_accounting.order.dto.create.CreateOrderRequest;
import com.ua.client_accounting.order.dto.create.CreateOrderResponse;
import com.ua.client_accounting.order.dto.update.UpdateOrderRequest;
import com.ua.client_accounting.order.dto.update.UpdateOrderResponse;
import com.ua.client_accounting.order.entity.Order;
import com.ua.client_accounting.order.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/V2/orders")
@AllArgsConstructor
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orderList = orderService.getAllOrders();
        return ResponseEntity.ok(orderList);
    }

    @PostMapping("/create")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        CreateOrderResponse response = orderService.createOrder(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<UpdateOrderResponse> updateOrder(@PathVariable UUID id, @RequestBody UpdateOrderRequest request) {
        UpdateOrderResponse response = orderService.updateOrder(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
