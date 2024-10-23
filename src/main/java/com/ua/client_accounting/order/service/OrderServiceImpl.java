package com.ua.client_accounting.order.service;

import com.ua.client_accounting.car.entity.Car;
import com.ua.client_accounting.car.repository.CarRepository;
import com.ua.client_accounting.order.dto.OrderDTO;
import com.ua.client_accounting.order.dto.OrderServiceDTO;
import com.ua.client_accounting.order.dto.create.CreateOrderRequest;
import com.ua.client_accounting.order.dto.create.CreateOrderResponse;
import com.ua.client_accounting.order.dto.update.UpdateOrderRequest;
import com.ua.client_accounting.order.dto.update.UpdateOrderResponse;
import com.ua.client_accounting.order.entity.Order;
import com.ua.client_accounting.order.entity.OrderServicePriceEntity;
import com.ua.client_accounting.order.repository.OrderRepository;
import com.ua.client_accounting.order.repository.OrderServiceRepository;
import com.ua.client_accounting.price.entity.ServicePrice;
import com.ua.client_accounting.price.repository.PriceRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;
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

    @Autowired
    private OrderServiceRepository orderServiceRepository;

    @Override
    public OrderDTO getOrderById(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResolutionException("Order with ID " + id + " not found"));

        List<OrderServiceDTO> serviceDTOList = order.getOrderServicePriceEntityList().stream()
                .map(orderServicePriceEntity -> new OrderServiceDTO(
                        orderServicePriceEntity.getServicePrice().getServiceName(),
                        orderServicePriceEntity.getServicePrice().getPrice()))
                .collect(Collectors.toList());

        return new OrderDTO(
                order.getId(),
                order.getCar().getId(),
                order.getOrderDate(),
                serviceDTOList
        );
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(orderEntity -> {
            List<OrderServiceDTO> serviceDTOList = orderEntity.getOrderServicePriceEntityList().stream()
                    .map(orderServicePriceEntity -> new OrderServiceDTO(
                            orderServicePriceEntity.getServicePrice().getServiceName(),
                            orderServicePriceEntity.getServicePrice().getPrice()))
                    .collect(Collectors.toList());

            return new OrderDTO(
                    orderEntity.getId(),
                    orderEntity.getCar().getId(),
                    orderEntity.getOrderDate(),
                    serviceDTOList
            );
        }).collect(Collectors.toList());
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

        orderRepository.save(order);

        servicePriceSet.forEach(servicePrice -> {
            OrderServicePriceEntity orderServicePriceEntity = new OrderServicePriceEntity();
            orderServicePriceEntity.setOrder(order);
            orderServicePriceEntity.setServicePrice(servicePrice);
            orderServiceRepository.save(orderServicePriceEntity);
        });

        CreateOrderResponse response = new CreateOrderResponse();
        response.setId(order.getId());

        return response;
    }

    @Transactional
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

        orderServiceRepository.deleteByOrder(existingOrder);

        servicePriceSet.forEach(servicePrice -> {
            OrderServicePriceEntity orderServicePriceEntity = new OrderServicePriceEntity();
            orderServicePriceEntity.setOrder(existingOrder);
            orderServicePriceEntity.setServicePrice(servicePrice);
            orderServiceRepository.save(orderServicePriceEntity);
        });

        Order updatedOrder = orderRepository.save(existingOrder);

        UpdateOrderResponse response = new UpdateOrderResponse();
        response.setId(updatedOrder.getId());
        response.setCar(updatedOrder.getCar().getId());
        response.setOrderDate(updatedOrder.getOrderDate());
        response.setServiceIds(updatedOrder.getOrderServicePriceEntityList().stream()
                .map(orderServicePriceEntity -> orderServicePriceEntity.getServicePrice().getId())
                .collect(Collectors.toSet()));

        return response;
    }

    @Transactional
    @Override
    public void deleteOrder(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order with ID " + id + " not found"));
        orderServiceRepository.deleteByOrder(order);
        orderRepository.delete(order);
    }
}
