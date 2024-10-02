package com.ua.client_accounting.table.controller;

import com.ua.client_accounting.table.dto.MainTableDTO;
import com.ua.client_accounting.car.entity.Car;
import com.ua.client_accounting.table.service.MainTableService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.action.internal.EntityActionVetoException;
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
    public ResponseEntity<List<MainTableDTO>> getClientCars() {
        List<MainTableDTO> clientCarDTOS = mainTableService.getClientCars();
        return ResponseEntity.ok(clientCarDTOS);
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
    public ResponseEntity<Car> createCarWithClient(@RequestBody MainTableDTO mainTableDTO) {
        Car car = mainTableService.createCarWithClient(mainTableDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(car);
    }

    @PutMapping("/edit/{carId}")
    public ResponseEntity<Car> updateCarWithClient(@PathVariable UUID carId, @RequestBody MainTableDTO mainTableDTO) {
        try {
            Car updateCar = mainTableService.updateCarWithClient(carId, mainTableDTO);
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
