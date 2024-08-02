package com.ua.client_accounting.car.service;

import com.ua.client_accounting.car.dto.create.CreateCarRequest;
import com.ua.client_accounting.car.dto.create.CreateCarResponse;
import com.ua.client_accounting.car.dto.update.UpdateCarRequest;
import com.ua.client_accounting.car.dto.update.UpdateCarResponse;
import com.ua.client_accounting.car.entity.Car;

import java.util.List;
import java.util.UUID;

public interface CarService {
    List<Car> getAllCars();
    CreateCarResponse createCar(CreateCarRequest request);
    Car getCarById(UUID carId);
    void deleteCar(UUID id);
    UpdateCarResponse updateCar(UUID carId, UpdateCarRequest request);
}
