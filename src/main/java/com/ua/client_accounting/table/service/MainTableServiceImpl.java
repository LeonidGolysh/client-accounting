package com.ua.client_accounting.table.service;

import com.ua.client_accounting.exception.order.CarModelNotFoundException;
import com.ua.client_accounting.exception.order.OrderNotFoundException;
import com.ua.client_accounting.exception.price.ServiceNotFoundException;
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
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MainTableServiceImpl implements MainTableService {

    private final ClientRepository clientRepository;
    private final CarRepository carRepository;
    private final PriceRepository priceRepository;
    private final OrderRepository orderRepository;
    private final OrderServiceRepository orderServiceRepository;
    private final ServiceIdConverter converterId = new ServiceIdConverter();

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MainTableDTO> getAllOrders() {
        return entityManager.createQuery(
                "SELECT new com.ua.client_accounting.table.dto.MainTableDTO(" +
                        "orders.id, client.name, client.phoneNumber, " +
                        "car.carModel, car.carColor, car.carNumberPlate, " +
                        "STRING_AGG(service.serviceName, ', '), " +
                        "orders.orderDate, SUM(service.price)) " +
                        "FROM Order orders " +
                        "JOIN orders.car car " +
                        "JOIN car.client client " +
                        "JOIN orders.orderServicePriceEntityList osp " +
                        "JOIN osp.servicePrice service " +
                        "GROUP BY orders.id, client.name, client.phoneNumber, " +
                        "car.carModel, car.carColor, car.carNumberPlate, orders.orderDate " +
                        "ORDER BY client.name, orders.orderDate", MainTableDTO.class
        ).getResultList();
    }

    @Override
    public MainTableDTO getOrderById(UUID orderId) {
        MainTableDTO results;
        try {
            results = entityManager.createQuery(
                    "SELECT new com.ua.client_accounting.table.dto.MainTableDTO(" +
                            "orders.id, client.name, client.phoneNumber, " +
                            "car.carModel, car.carColor, car.carNumberPlate, " +
                            "STRING_AGG(service.serviceName, ', '), " +
                            "orders.orderDate, SUM(service.price)) " +
                            "FROM Order orders " +
                            "JOIN orders.car car " +
                            "JOIN car.client client " +
                            "JOIN orders.orderServicePriceEntityList osp " +
                            "JOIN osp.servicePrice service " +
                            "WHERE orders.id = :orderId " +
                            "GROUP BY orders.id, client.name, client.phoneNumber, " +
                            "car.carModel, car.carColor, car.carNumberPlate, orders.orderDate " +
                            "ORDER BY client.name, orders.orderDate", MainTableDTO.class
            ).setParameter("orderId", orderId).getSingleResult();
        } catch (NoResultException e) {
            throw new OrderNotFoundException("Order with ID: " + orderId + " not found");
        }

        return results;
    }

    @Override
    public List<MainTableDTO> getOrderCarByModel(String carModel) {
        List<MainTableDTO> results = entityManager.createQuery(
                "SELECT new com.ua.client_accounting.table.dto.MainTableDTO(" +
                        "orders.id, client.name, client.phoneNumber, " +
                        "car.carModel, car.carColor, car.carNumberPlate, " +
                        "STRING_AGG(service.serviceName, ', '), " +
                        "orders.orderDate, SUM(service.price)) " +
                        "FROM Order orders " +
                        "JOIN orders.car car " +
                        "JOIN car.client client " +
                        "JOIN orders.orderServicePriceEntityList osp " +
                        "JOIN osp.servicePrice service " +
                        "WHERE car.carModel = :carModel " +
                        "GROUP BY orders.id, client.name, client.phoneNumber, " +
                        "car.carModel, car.carColor, car.carNumberPlate, orders.orderDate " +
                        "ORDER BY client.name, orders.orderDate", MainTableDTO.class
        ).setParameter("carModel", carModel).getResultList();

        if (results.isEmpty()) {
            throw new CarModelNotFoundException(carModel + " not found");
        }

        return results;
    }

    @Override
    @Transactional
    public MainTableDTO createOrder(MainTableDTO mainTableDTO) {
        Client client = getOrCreateClient(mainTableDTO);
        Car car = getOrCreateCar(mainTableDTO, client);

        Set<Long> serviceIds = converterId.convertServicesToIds(mainTableDTO.getServices());
        Set<ServicePrice> servicePriceSet = getServicePrice(serviceIds);

        Order order = new Order();
        order.setCar(car);
        order.setOrderDate(mainTableDTO.getOrderDate());
        order.setOrderServicePriceEntityList(new ArrayList<>());

        addServicesToOrder(order, servicePriceSet);

        Order saveOrder = orderRepository.save(order);

        return MainTableDTO.fromDTO(saveOrder);
    }

    private Client getOrCreateClient(MainTableDTO mainTableDTO) {
        return clientRepository.findByNameAndPhoneNumber(mainTableDTO.getClientName(), mainTableDTO.getPhoneNumber())
                .orElseGet(() -> {
                    Client newClient = new Client();
                    newClient.setName(mainTableDTO.getClientName());
                    newClient.setPhoneNumber(mainTableDTO.getPhoneNumber());
                    return clientRepository.save(newClient);
                });
    }

    private Car getOrCreateCar(MainTableDTO mainTableDTO, Client client) {
        return carRepository.findByCarNumberPlate(mainTableDTO.getCarNumberPlate())
                .orElseGet(() -> {
                    Car newCar = new Car();
                    newCar.setClient(client);
                    newCar.setCarModel(mainTableDTO.getCarModel());
                    newCar.setCarColor(mainTableDTO.getCarColor());
                    newCar.setCarNumberPlate(mainTableDTO.getCarNumberPlate());
                    return carRepository.save(newCar);
                });
    }

    private Set<ServicePrice> getServicePrice(Set<Long> serviceIds) {
        return serviceIds.stream()
                .map(serviceId -> priceRepository.findById(serviceId)
                        .orElseThrow(() -> new ServiceNotFoundException("Service with ID: " + serviceId + " not found")))
                .collect(Collectors.toSet());
    }

    private void addServicesToOrder(Order order, Set<ServicePrice> servicePriceSet) {
        servicePriceSet.forEach(servicePrice -> {
            OrderServicePriceEntity orderServicePriceEntity = new OrderServicePriceEntity();
            orderServicePriceEntity.setOrder(order);
            orderServicePriceEntity.setServicePrice(servicePrice);

            order.getOrderServicePriceEntityList().add(orderServicePriceEntity);
        });
    }

    @Override
    @Transactional
    public MainTableDTO updateOrder(UUID orderId, MainTableDTO mainTableDTO) {
        Order existOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found"));

        Client updateClient = updateClient(existOrder.getCar().getClient(), mainTableDTO);
        Car updateCar = updateCar(existOrder.getCar(), mainTableDTO, updateClient);

        if (mainTableDTO.getServices() != null) {
            updateOrderServices(existOrder, mainTableDTO.getServices());
        }

        existOrder.setOrderDate(mainTableDTO.getOrderDate());
        existOrder.setCar(updateCar);

        Order saveOrder = orderRepository.save(existOrder);

        return MainTableDTO.fromDTO(saveOrder);
    }

    private Client updateClient(Client client, MainTableDTO mainTableDTO) {
        if (client == null) {
            client = new Client();  //If client is not connected, create a new one
        }

        client.setName(mainTableDTO.getClientName());
        client.setPhoneNumber(mainTableDTO.getPhoneNumber());
        return clientRepository.save(client);
    }

    private Car updateCar(Car car, MainTableDTO mainTableDTO, Client client) {
        Optional<Car> existingCarOptional = carRepository.findByClientIdAndCarNumberPlate(
                client.getId(), mainTableDTO.getCarNumberPlate());

        if (existingCarOptional.isPresent()) {
            return existingCarOptional.get();
        }

        if (car != null && !car.getCarNumberPlate().equals(mainTableDTO.getCarNumberPlate())) {
            car = new Car();
        }

        car.setClient(client);
        car.setCarModel(mainTableDTO.getCarModel());
        car.setCarColor(mainTableDTO.getCarColor());
        car.setCarNumberPlate(mainTableDTO.getCarNumberPlate());
        return carRepository.save(car);
    }

    private void updateOrderServices(Order existOrder, Object services) {
        Set<Long> newServiceIds = converterId.convertServicesToIds(services);
        Set<Long> existingServiceIds = existOrder.getOrderServicePriceEntityList().stream()
                .map(orderServicePriceEntity -> orderServicePriceEntity.getServicePrice().getId())
                .collect(Collectors.toSet());

        List<OrderServicePriceEntity> toRemove = existOrder.getOrderServicePriceEntityList().stream()
                .filter(orderServicePriceEntity -> !newServiceIds.contains(orderServicePriceEntity.getServicePrice().getId()))
                .collect(Collectors.toList());
        toRemove.forEach(entity -> existOrder.getOrderServicePriceEntityList().remove(entity));

        Set<Long> toAdd = newServiceIds.stream()
                .filter(serviceId -> !existingServiceIds.contains(serviceId))
                .collect(Collectors.toSet());

        Set<ServicePrice> servicePriceSet = getServicePrice(toAdd);

        servicePriceSet.forEach(servicePrice -> {
            OrderServicePriceEntity orderServicePriceEntity = new OrderServicePriceEntity();
            orderServicePriceEntity.setOrder(existOrder);
            orderServicePriceEntity.setServicePrice(servicePrice);
            existOrder.getOrderServicePriceEntityList().add(orderServicePriceEntity);
        });
    }

    @Override
    @Transactional
    public void deleteOrder(UUID orderId) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found"));

        orderServiceRepository.deleteAll(existingOrder.getOrderServicePriceEntityList());

        Car car = existingOrder.getCar();
        Client client = car.getClient();

        orderRepository.delete(existingOrder);

        //Check if there are any orders left for this car
        List<Order> remainingOrdersForCar = orderRepository.findByCar(car);
        if (remainingOrdersForCar.isEmpty()) {
            carRepository.delete(car);

            List<Car> remainingCarsForClient = carRepository.findByClientId(client.getId());
            if (remainingCarsForClient.isEmpty()) {
                clientRepository.delete(client);
            }
        }
    }
}
