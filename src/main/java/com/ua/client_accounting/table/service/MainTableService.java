package com.ua.client_accounting.table.service;

import com.ua.client_accounting.table.dto.MainTableDTO;

import java.util.List;
import java.util.UUID;

public interface MainTableService {
    List<MainTableDTO> getAllOrders();
    MainTableDTO getOrderById(UUID orderId);
    List<MainTableDTO> getOrderCarByModel(String carModel);
    MainTableDTO createOrder(MainTableDTO mainTableDTO);
    MainTableDTO updateOrder(UUID orderId, MainTableDTO mainTableDTO);
    void deleteOrder(UUID orderId);
}
