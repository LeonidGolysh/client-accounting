package com.ua.client_accounting.table.service;

import com.sun.tools.javac.Main;
import com.ua.client_accounting.car.entity.Car;
import com.ua.client_accounting.car.repository.CarRepository;
import com.ua.client_accounting.client.entity.Client;
import com.ua.client_accounting.client.repository.ClientRepository;
import com.ua.client_accounting.exception.order.OrderNotFoundException;

import com.ua.client_accounting.exception.price.ServiceNotFoundException;
import com.ua.client_accounting.order.entity.Order;
import com.ua.client_accounting.order.entity.OrderServicePriceEntity;
import com.ua.client_accounting.order.entity.OrderServicePriceId;
import com.ua.client_accounting.order.repository.OrderRepository;
import com.ua.client_accounting.order.repository.OrderServiceRepository;
import com.ua.client_accounting.price.entity.ServicePrice;
import com.ua.client_accounting.price.repository.PriceRepository;
import com.ua.client_accounting.table.dto.MainTableDTO;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainTableServiceImplTest {
    static UUID orderId;
    static UUID clientId;
    static UUID carId;

    @Mock
    ClientRepository clientRepository;

    @Mock
    CarRepository carRepository;

    @Mock
    PriceRepository priceRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderServiceRepository orderServiceRepository;

    @InjectMocks
    MainTableServiceImpl mainTableService;

    @BeforeAll
    static void setUpBeforeClass() {
        orderId = UUID.randomUUID();
        clientId = UUID.randomUUID();
        carId = UUID.randomUUID();
    }

    @Test
    void createOrder_Success_Test() {
        MainTableDTO input = createMainTableDTO();

        when(clientRepository.findByNameAndPhoneNumber("John Doe", "1234567890")).thenReturn(Optional.empty());
        when(carRepository.findByCarNumberPlate("ABC123")).thenReturn(Optional.empty());
        when(priceRepository.findById(1L)).thenReturn(Optional.of(new ServicePrice(1L, "Service 1", new BigDecimal("100.00"))));
        when(priceRepository.findById(2L)).thenReturn(Optional.of(new ServicePrice(2L, "Service 2", new BigDecimal("200.00"))));

        Client savedClient = createClient();
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        Car savedCar = createCar();
        when(carRepository.save(any(Car.class))).thenReturn(savedCar);

        List<OrderServicePriceEntity> savedOrderServices = Arrays.asList(
                new OrderServicePriceEntity(new OrderServicePriceId(orderId, 1L), null, new ServicePrice(1L, "Service 1", new BigDecimal("100.00"))),
                new OrderServicePriceEntity(new OrderServicePriceId(orderId, 2L), null, new ServicePrice(2L, "Service 2", new BigDecimal("200.00")))
        );

        Order savedOrder = new Order(orderId, savedCar, LocalDateTime.now(), savedOrderServices);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        MainTableDTO result = mainTableService.createOrder(input);

        assertNotNull(result.getOrderId());
        assertEquals("John Doe", result.getClientName());
        assertEquals("1234567890", result.getPhoneNumber());
        assertEquals("BMW", result.getCarModel());
        assertEquals(0, result.getTotalPrice().compareTo(new BigDecimal("300.00")));

        verify(clientRepository).save(any(Client.class));
        verify(carRepository).save(any(Car.class));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_serviceNotFoundTest() {
        MainTableDTO input = new MainTableDTO(null, "John Doe", "1234567890", "BMW", "Black", "ABC123", "1, 99", LocalDateTime.now(), null);

        when(priceRepository.findById(1L)).thenReturn(Optional.of(new ServicePrice(1L, "Service 1", new BigDecimal("100.00"))));
        when(priceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ServiceNotFoundException.class, () -> mainTableService.createOrder(input));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_invalidServiceFormatTest() {
        MainTableDTO input = new MainTableDTO(null, "John Doe", "1234567890", "BMW", "Black", "ABC123", "abc, xyz", LocalDateTime.now(), null);

        assertThrows(IllegalArgumentException.class, () -> mainTableService.createOrder(input));
        verify(orderRepository, never()).save(any());
    }



    @Test
    void updateOrder_orderNotFoundTest() {
        MainTableDTO input = new MainTableDTO();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> mainTableService.updateOrder(orderId, input));

        verify(orderRepository, never()).save(any());
    }

    private MainTableDTO createMainTableDTO() {
        return MainTableDTO.builder()
                .orderId(null)
                .clientName("John Doe")
                .phoneNumber("1234567890")
                .carModel("BMW")
                .carColor("Black")
                .carNumberPlate("ABC123")
                .services("1, 2")
                .orderDate(LocalDateTime.now())
                .totalPrice(null)
                .build();
    }

    private Client createClient() {
        return Client.builder()
                .id(clientId)
                .name("John Doe")
                .phoneNumber("1234567890")
                .build();
    }

    private Car createCar() {
        return Car.builder()
                .id(carId)
                .client(createClient())
                .carModel("BMW")
                .carColor("Black")
                .carNumberPlate("ABC123")
                .build();
    }

    @Test
    void deleteOrderTest() {
        Client client = createClient();

        Car car = createCar();

        Order order = new Order();
        order.setId(orderId);
        order.setCar(car);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderServicePriceEntityList(new ArrayList<>());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.findByCar(car)).thenReturn(Collections.emptyList());
        when(carRepository.findByClientId(clientId)).thenReturn(Collections.emptyList());

        mainTableService.deleteOrder(orderId);

        verify(orderServiceRepository).deleteAll(order.getOrderServicePriceEntityList());
        verify(orderRepository).delete(order);
        verify(carRepository).delete(car);
        verify(clientRepository).delete(client);
    }

    @Test
    void deleteOrder_exceptionOrderNotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> mainTableService.deleteOrder(orderId));

        verify(orderServiceRepository, never()).deleteAll();
        verify(orderRepository, never()).delete(any());
        verify(carRepository, never()).delete(any());
        verify(clientRepository, never()).delete(any());
    }

    @Test
    void deleteOrder_notDeleteCarOrClientIfOrderRemain() {
        Client client = new Client();
        client.setId(clientId);

        Car car = new Car();
        car.setId(carId);
        car.setClient(client);

        Order order = new Order();
        order.setId(orderId);
        order.setCar(car);
        order.setOrderServicePriceEntityList(new ArrayList<>());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.findByCar(car)).thenReturn(List.of(new Order()));

        mainTableService.deleteOrder(orderId);

        verify(orderServiceRepository).deleteAll(order.getOrderServicePriceEntityList());
        verify(orderRepository).delete(order);
        verify(carRepository, never()).delete(car);
        verify(clientRepository, never()).delete(client);
    }
}
