package com.ua.client_accounting.table.service;

import com.ua.client_accounting.car.entity.Car;
import com.ua.client_accounting.table.dto.MainTableDTO;

import java.util.List;
import java.util.UUID;

public interface MainTableService {
    List<MainTableDTO> getClientCars();
    MainTableDTO getClientCarById(UUID carId);
    List<MainTableDTO> getClientCarByModel(String carModel);
    Car createCarWithClient(MainTableDTO mainTableDTO);
    Car updateCarWithClient(UUID carId, MainTableDTO mainTableDTO);
}
