package com.ua.client_accounting.order.service;

import com.ua.client_accounting.order.dto.create.CreateOrderRequest;
import com.ua.client_accounting.order.dto.create.CreateOrderResponse;
import com.ua.client_accounting.order.dto.update.UpdateOrderRequest;
import com.ua.client_accounting.order.dto.update.UpdateOrderResponse;
import com.ua.client_accounting.order.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order getOrderById(UUID id);

    List<Order> getAllOrders();

    CreateOrderResponse createOrder(CreateOrderRequest request);

    UpdateOrderResponse updateOrder(UUID id, UpdateOrderRequest request);

    void deleteOrder(UUID id);
}
