package com.ua.client_accounting.order.service;

import com.ua.client_accounting.order.dto.OrderDTO;
import com.ua.client_accounting.order.dto.create.CreateOrderRequest;
import com.ua.client_accounting.order.dto.create.CreateOrderResponse;
import com.ua.client_accounting.order.dto.update.UpdateOrderRequest;
import com.ua.client_accounting.order.dto.update.UpdateOrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderDTO getOrderById(UUID id);

    List<OrderDTO> getAllOrders();

    CreateOrderResponse createOrder(CreateOrderRequest request);

    UpdateOrderResponse updateOrder(UUID id, UpdateOrderRequest request);

    void deleteOrder(UUID id);
}
