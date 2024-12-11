package com.ua.client_accounting.table.controller;

import com.ua.client_accounting.table.dto.MainTableDTO;
import com.ua.client_accounting.table.service.MainTableService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
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
    public ResponseEntity<MainTableDTO> getOrderCarById(@PathVariable("id") UUID orderId) {
        MainTableDTO orderDTO = mainTableService.getOrderById(orderId);
        if (orderDTO != null) {
            return ResponseEntity.ok(orderDTO);
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
    public ResponseEntity<MainTableDTO> createOrder(@RequestBody MainTableDTO mainTableDTO) {
        MainTableDTO order = mainTableService.createOrder(mainTableDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping("/edit/{orderId}")
    public ResponseEntity<MainTableDTO> updateOrder(@PathVariable UUID orderId, @RequestBody MainTableDTO mainTableDTO) {
        try {
            MainTableDTO updateOrder = mainTableService.updateOrder(orderId, mainTableDTO);

            return ResponseEntity.ok(updateOrder);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId) {
        try {
            mainTableService.deleteOrder(orderId);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
