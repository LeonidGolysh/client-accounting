package com.ua.client_accounting.table.service;

import com.ua.client_accounting.order.entity.Order;
import com.ua.client_accounting.order.entity.OrderServicePriceEntity;
import com.ua.client_accounting.order.repository.OrderRepository;
import com.ua.client_accounting.order.repository.OrderServiceRepository;
import com.ua.client_accounting.price.entity.ServicePrice;
import com.ua.client_accounting.price.repository.PriceRepository;
import com.ua.client_accounting.table.dto.MainTableDTO;

import com.ua.client_accounting.car.entity.Car;
import com.ua.client_accounting.car.repository.CarRepository;
import com.ua.client_accounting.client.entity.Client;
import com.ua.client_accounting.client.repository.ClientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MainTableServiceImpl implements MainTableService{

    private final ClientRepository clientRepository;
    private final CarRepository carRepository;
    private final PriceRepository priceRepository;
    private final OrderRepository orderRepository;
    private final OrderServiceRepository orderServiceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<MainTableDTO> getAllClients() {
        return entityManager.createQuery(
                "SELECT new com.ua.client_accounting.table.dto.MainTableDTO(" +
                        "car.id, client.name, client.phoneNumber, " +
                        "car.carModel, car.carColor, car.carNumberPlate, " +
                        "STRING_AGG(service.serviceName, ', '), " +
                        "orders.orderDate, SUM(service.price)) " +
                        "FROM Order orders " +
                        "JOIN orders.car car " +
                        "JOIN car.client client " +
                        "JOIN orders.orderServicePriceEntityList osp " +
                        "JOIN osp.servicePrice service " +
                        "GROUP BY car.id, client.name, client.phoneNumber, " +
                        "car.carModel, car.carColor, car.carNumberPlate, orders.orderDate " +
                        "ORDER BY client.name, orders.orderDate", MainTableDTO.class
        ).getResultList();
    }

    public MainTableDTO getClientCarById(UUID carId) {
        return entityManager.createQuery(
                "SELECT new com.ua.client_accounting.table.dto.MainTableDTO(" +
                        "car.id, client.name, client.phoneNumber, " +
                        "car.carModel, car.carColor, car.carNumberPlate, " +
                        "STRING_AGG(service.serviceName, ', '), " +
                        "orders.orderDate, SUM(service.price)) " +
                        "FROM Order orders " +
                        "JOIN orders.car car " +
                        "JOIN car.client client " +
                        "JOIN orders.orderServicePriceEntityList osp " +
                        "JOIN osp.servicePrice service " +
                        "WHERE car.id = :carId " +
                        "GROUP BY car.id, client.name, client.phoneNumber, " +
                        "car.carModel, car.carColor, car.carNumberPlate, orders.orderDate " +
                        "ORDER BY client.name, orders.orderDate", MainTableDTO.class
        ).setParameter("carId", carId).getSingleResult();
    }

    public List<MainTableDTO> getClientCarByModel(String carModel) {
        return entityManager.createQuery(
                "SELECT new com.ua.client_accounting.table.dto.MainTableDTO(" +
                        "car.id, client.name, client.phoneNumber, " +
                        "car.carModel, car.carColor, car.carNumberPlate, " +
                        "STRING_AGG(service.serviceName, ', '), " +
                        "orders.orderDate, SUM(service.price)) " +
                        "FROM Order orders " +
                        "JOIN orders.car car " +
                        "JOIN car.client client " +
                        "JOIN orders.orderServicePriceEntityList osp " +
                        "JOIN osp.servicePrice service " +
                        "WHERE car.carModel = :carModel " +
                        "GROUP BY car.id, client.name, client.phoneNumber, " +
                        "car.carModel, car.carColor, car.carNumberPlate, orders.orderDate " +
                        "ORDER BY client.name, orders.orderDate", MainTableDTO.class
        ).setParameter("carModel", carModel).getResultList();
    }

    @Transactional
    public Order createCarWithClient(MainTableDTO mainTableDTO) {
        Client client = clientRepository.findByNameAndPhoneNumber(mainTableDTO.getClientName(), mainTableDTO.getPhoneNumber())
                .orElseGet(() -> {
                    Client newClient = new Client();
                    newClient.setName(mainTableDTO.getClientName());
                    newClient.setPhoneNumber(mainTableDTO.getPhoneNumber());
                    return clientRepository.save(newClient);
                });

        Car car = carRepository.findByCarNumberPlate(mainTableDTO.getCarNumberPlate())
                .orElseGet(() -> {
                    Car newCar = new Car();
                    newCar.setClient(client);
                    newCar.setCarModel(mainTableDTO.getCarModel());
                    newCar.setCarColor(mainTableDTO.getCarColor());
                    newCar.setCarNumberPlate(mainTableDTO.getCarNumberPlate());
                    return carRepository.save(newCar);
                });

        Set<Long> serviceIds = convertServicesToIds(mainTableDTO.getServices());

        Set<ServicePrice> servicePriceSet = serviceIds.stream()
                .map(serviceId -> priceRepository.findById(serviceId)
                        .orElseThrow(() -> new RuntimeException("Service not found")))
                .collect(Collectors.toSet());

        Order order = new Order();
        order.setCar(car);
        order.setOrderDate(mainTableDTO.getOrderDate());
        order.setOrderServicePriceEntityList(new ArrayList<>());

        servicePriceSet.forEach(servicePrice -> {
            OrderServicePriceEntity orderServicePriceEntity = new OrderServicePriceEntity();
            orderServicePriceEntity.setOrder(order);
            orderServicePriceEntity.setServicePrice(servicePrice);

            order.getOrderServicePriceEntityList().add(orderServicePriceEntity);
        });
        orderRepository.save(order);

        return order;
    }

    private Set<Long> convertServicesToIds(Object services) {
        Set<Long> serviceIds = new HashSet<>();

        if (services instanceof String) {
            try {
                serviceIds = Arrays.stream(((String) services).split(","))
                        .map(String::trim)
                        .map(Long::parseLong)
                        .collect(Collectors.toSet());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid format in services string: " + serviceIds, e);
            }
        } else if (services instanceof Collection<?>) {
            serviceIds = ((Collection<?>) services).stream()
                    .map(Object::toString)
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());
        } else {
            throw new IllegalArgumentException("Unsupported services type: " + services.getClass().getName());
        }
        return serviceIds;
    }

    @Transactional
    public Order updateCarWithClient(UUID orderId, MainTableDTO mainTableDTO) {
        Order existOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with ID " + orderId + " not found"));

        Car car = existOrder.getCar();

        Client client = car.getClient();
        if (client == null) {
            client = new Client();  //If client is not connected, create a new one
        }

        client.setName(mainTableDTO.getClientName());
        client.setPhoneNumber(mainTableDTO.getPhoneNumber());

        client = clientRepository.save(client);

        car.setClient(client);
        car.setCarModel(mainTableDTO.getCarModel());
        car.setCarColor(mainTableDTO.getCarColor());
        car.setCarColor(mainTableDTO.getCarColor());
        car.setCarNumberPlate(mainTableDTO.getCarNumberPlate());
        carRepository.save(car);

        if (mainTableDTO.getServices() != null) {
            Set<Long> serviceIds = convertServicesToIds(mainTableDTO.getServices());

            Set<ServicePrice> servicePriceSet = serviceIds.stream()
                    .map(serviceId -> priceRepository.findById(serviceId)
                            .orElseThrow(() -> new RuntimeException("Service not found ID: " + serviceId)))
                    .collect(Collectors.toSet());

                List<OrderServicePriceEntity> updatedServiceList = new ArrayList<>();
                servicePriceSet.forEach(servicePrice -> {
                    OrderServicePriceEntity orderServicePriceEntity = new OrderServicePriceEntity();
                    orderServicePriceEntity.setOrder(existOrder);
                    orderServicePriceEntity.setServicePrice(servicePrice);
                    updatedServiceList.add(orderServicePriceEntity);
                });
                existOrder.getOrderServicePriceEntityList().clear();
                existOrder.getOrderServicePriceEntityList().addAll(updatedServiceList);
        }

        existOrder.setOrderDate(mainTableDTO.getOrderDate());
        return orderRepository.save(existOrder);
    }

    @Transactional
    public void deleteCarAndClientIfNoMoreCars(UUID carId) {
        Car existingCar = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Car with ID " + carId + " not found"));

        Client client = existingCar.getClient();

        List<Order> orders = orderRepository.findByCar(existingCar);
        for (Order order : orders) {
            orderServiceRepository.deleteAll(order.getOrderServicePriceEntityList());

            orderRepository.delete(order);
        }

        carRepository.delete(existingCar);

        List<Car> remainingCars = carRepository.findByClientId(client.getId());
        if (remainingCars.isEmpty()) {
            clientRepository.delete(client);
        }
    }
}
