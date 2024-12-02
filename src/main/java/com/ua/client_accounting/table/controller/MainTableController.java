package com.ua.client_accounting.table.controller;

import com.ua.client_accounting.order.entity.Order;
import com.ua.client_accounting.table.dto.MainTableDTO;
import com.ua.client_accounting.table.service.MainTableService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients-accounting")
@AllArgsConstructor
public class MainTableController {

    private MainTableService mainTableService;

    @GetMapping
    public ResponseEntity<List<MainTableDTO>> getAll() {
        List<MainTableDTO> allOrders = mainTableService.getAllOrders();
        return ResponseEntity.ok(allOrders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MainTableDTO> getOrderCarById(@PathVariable("id") UUID carId) {
        MainTableDTO orderCarDTO = mainTableService.getOrderCarById(carId);
        if (orderCarDTO != null) {
            return ResponseEntity.ok(orderCarDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/model/{carModel}")
    public ResponseEntity<List<MainTableDTO>> getOrderCarsByModel(@PathVariable("carModel") String carModel) {
        List<MainTableDTO> orderCarDTOs = mainTableService.getOrderCarByModel(carModel);
        if (!orderCarDTOs.isEmpty()) {
            return ResponseEntity.ok(orderCarDTOs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrderInfo(@RequestBody MainTableDTO mainTableDTO) {
        Order order = mainTableService.createOrderInfo(mainTableDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping("/edit/{orderId}")
    public ResponseEntity<Order> updateOrderInfo(@PathVariable UUID orderId, @RequestBody MainTableDTO mainTableDTO) {
        try {
            Order updateOrder = mainTableService.updateOrderInfo(orderId, mainTableDTO);

            Hibernate.initialize(updateOrder.getCar());
            Hibernate.initialize(updateOrder.getOrderServicePriceEntityList());
            updateOrder.getOrderServicePriceEntityList().forEach(entity ->
                    Hibernate.initialize(entity.getServicePrice()));

            return ResponseEntity.ok(updateOrder);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{carId}")
    public ResponseEntity<Void> deleteOrderInfo(@PathVariable UUID carId) {
        try {
            mainTableService.deleteOrderInfo(carId);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
