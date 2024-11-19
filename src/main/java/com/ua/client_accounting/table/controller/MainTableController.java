package com.ua.client_accounting.table.controller;

import com.ua.client_accounting.order.dto.OrderDTO;
import com.ua.client_accounting.order.dto.create.CreateOrderResponse;
import com.ua.client_accounting.order.entity.Order;
import com.ua.client_accounting.table.dto.MainTableDTO;
import com.ua.client_accounting.car.entity.Car;
import com.ua.client_accounting.table.service.MainTableService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/V2/client-car")
@AllArgsConstructor
public class MainTableController {

    private MainTableService mainTableService;

    @GetMapping
    public ResponseEntity<List<MainTableDTO>> getAll() {
        List<MainTableDTO> allClients = mainTableService.getAllClients();
        return ResponseEntity.ok(allClients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MainTableDTO> getClientCarById(@PathVariable("id") UUID carId) {
        MainTableDTO clientCarDTO = mainTableService.getClientCarById(carId);
        if (clientCarDTO != null) {
            return ResponseEntity.ok(clientCarDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/model/{carModel}")
    public ResponseEntity<List<MainTableDTO>> getClientCarsByModel(@PathVariable("carModel") String carModel) {
        List<MainTableDTO> clientCarDTOs = mainTableService.getClientCarByModel(carModel);
        if (!clientCarDTOs.isEmpty()) {
            return ResponseEntity.ok(clientCarDTOs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createCarWithClient(@RequestBody MainTableDTO mainTableDTO) {
        Order order = mainTableService.createCarWithClient(mainTableDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping("/edit/{orderId}")
    public ResponseEntity<Order> updateCarWithClient(@PathVariable UUID orderId, @RequestBody MainTableDTO mainTableDTO) {
        try {
            Order updateCar = mainTableService.updateCarWithClient(orderId, mainTableDTO);
            return ResponseEntity.ok(updateCar);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{carId}")
    public ResponseEntity<Void> deleteCarAndClient(@PathVariable UUID carId) {
        try {
            mainTableService.deleteCarAndClientIfNoMoreCars(carId);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
