package com.ua.client_accounting.table.service;

import com.ua.client_accounting.order.entity.Order;
import com.ua.client_accounting.table.dto.MainTableDTO;

import java.util.List;
import java.util.UUID;

public interface MainTableService {
    List<MainTableDTO> getAllOrders();
    List<MainTableDTO> getOrderCarById(UUID carId);
    List<MainTableDTO> getOrderCarByModel(String carModel);
    Order createOrder(MainTableDTO mainTableDTO);
    Order updateOrder(UUID orderId, MainTableDTO mainTableDTO);
    void deleteOrder(UUID orderId);
}
