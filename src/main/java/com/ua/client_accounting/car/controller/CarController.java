package com.ua.client_accounting.car.controller;

import com.ua.client_accounting.car.dto.create.CreateCarRequest;
import com.ua.client_accounting.car.dto.create.CreateCarResponse;
import com.ua.client_accounting.car.dto.update.UpdateCarRequest;
import com.ua.client_accounting.car.dto.update.UpdateCarResponse;
import com.ua.client_accounting.car.entity.Car;
import com.ua.client_accounting.car.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/V2/cars")
@AllArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    @PostMapping("/create")
    public ResponseEntity<CreateCarResponse> createCar(@RequestBody CreateCarRequest request) {
        CreateCarResponse response = carService.createCar(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable UUID carId) {
        Car cars = carService.getCarById(carId);
        return ResponseEntity.ok(cars);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<UpdateCarResponse> updateCar(@PathVariable UUID id, @RequestBody UpdateCarRequest request) {
        UpdateCarResponse response = carService.updateCar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
