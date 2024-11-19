package com.ua.client_accounting.table.service;

import com.ua.client_accounting.order.entity.Order;
import com.ua.client_accounting.table.dto.MainTableDTO;

import java.util.List;
import java.util.UUID;

public interface MainTableService {
    List<MainTableDTO> getAllClients();
    MainTableDTO getClientCarById(UUID carId);
    List<MainTableDTO> getClientCarByModel(String carModel);
    Order createCarWithClient(MainTableDTO mainTableDTO);
    Order updateCarWithClient(UUID orderId, MainTableDTO mainTableDTO);
    void deleteCarAndClientIfNoMoreCars(UUID carId);
}
