package com.ua.client_accounting.order.service;

import com.ua.client_accounting.car.entity.Car;
import com.ua.client_accounting.car.repository.CarRepository;
import com.ua.client_accounting.order.dto.create.CreateOrderRequest;
import com.ua.client_accounting.order.dto.create.CreateOrderResponse;
import com.ua.client_accounting.order.dto.update.UpdateOrderRequest;
import com.ua.client_accounting.order.dto.update.UpdateOrderResponse;
import com.ua.client_accounting.order.entity.Order;
import com.ua.client_accounting.order.repository.OrderRepository;
import com.ua.client_accounting.price.entity.ServicePrice;
import com.ua.client_accounting.price.repository.PriceRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Override
    public Order getOrderById(UUID id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order with ID " + id + " not found"));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        Car car = carRepository.findById(request.getCar()).orElseThrow(() -> new RuntimeException("Car not found"));

        Set<ServicePrice> servicePriceSet = request.getServiceIds().stream()
                .map(priceRepository::findById)
                .map(opt -> opt.orElseThrow(() -> new RuntimeException("Service not found")))
                .collect(Collectors.toSet());

        Order order = new Order();
        order.setCar(car);
        order.setOrderDate(request.getOrderDate());
        order.setServicePriceSet(servicePriceSet);

        orderRepository.save(order);
        CreateOrderResponse response = new CreateOrderResponse();
        response.setId(order.getId());

        return response;
    }

    @Override
    public UpdateOrderResponse updateOrder(UUID id, UpdateOrderRequest request) {
        Order existingOrder = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order with ID " + id + " not found"));
        Car car = carRepository.findById(request.getCar()).orElseThrow(() -> new RuntimeException("Car not found"));

        Set<ServicePrice> servicePriceSet = request.getServiceIds().stream()
                .map(priceRepository::findById)
                .map(opt -> opt.orElseThrow(() -> new RuntimeException("Service not found")))
                .collect(Collectors.toSet());

        existingOrder.setCar(car);
        existingOrder.setOrderDate(request.getOrderDate());
        existingOrder.setServicePriceSet(servicePriceSet);

        Order updatedOrder = orderRepository.save(existingOrder);

        UpdateOrderResponse response = new UpdateOrderResponse();
        response.setId(updatedOrder.getId());
        response.setCar(updatedOrder.getCar().getId());
        response.setOrderDate(updatedOrder.getOrderDate());
        response.setServiceIds(updatedOrder.getServicePriceSet().stream()
                .map(ServicePrice::getId)
                .collect(Collectors.toSet()));

        return response;
    }

    @Override
    public void deleteOrder(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order with ID " + id + " not found"));
        orderRepository.delete(order);
    }
}
