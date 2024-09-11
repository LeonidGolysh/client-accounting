package com.ua.client_accounting.car.service;

import com.ua.client_accounting.car.dto.ClientCarDTO;

import com.ua.client_accounting.car.entity.Car;
import com.ua.client_accounting.car.repository.CarRepository;
import com.ua.client_accounting.client.entity.Client;
import com.ua.client_accounting.client.repository.ClientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientCarServiceImpl {

    private final ClientRepository clientRepository;
    private final CarRepository carRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<ClientCarDTO> getClientCars() {
        Query query = entityManager.createQuery(
                "SELECT new com.ua.client_accounting.car.dto.ClientCarDTO(" +
                        "car.id, client.name, client.phoneNumber, " +
                        "car.carModel, car.carColor, car.carNumberPlate) " +
                        "FROM Car car JOIN car.client client"
        );
        return query.getResultList();
    }

    public ClientCarDTO getClientCarById(UUID carId) {
        Query query = entityManager.createQuery(
                "SELECT new com.ua.client_accounting.car.dto.ClientCarDTO(" +
                        "car.id, client.name, client.phoneNumber, " +
                        "car.carModel, car.carColor, car.carNumberPlate) " +
                        "FROM Car car JOIN car.client client " +
                        "WHERE car.id = :carId"
        );
        query.setParameter("carId", carId);
        return (ClientCarDTO) query.getSingleResult();
    }

    public List<ClientCarDTO> getClientCarByModel(String carModel) {
        Query query = entityManager.createQuery(
                "SELECT new com.ua.client_accounting.car.dto.ClientCarDTO(" +
                        "car.id, client.name, client.phoneNumber, " +
                        "car.carModel, car.carColor, car.carNumberPlate) " +
                        "FROM Car car JOIN car.client client " +
                        "WHERE car.carModel = :carModel"
        );
        query.setParameter("carModel", carModel);
        return query.getResultList();
    }

    @Transactional
    public Car createCarWithClient(ClientCarDTO clientCarDTO) {
        Client client = new Client();
        client.setName(clientCarDTO.getClientName());
        client.setPhoneNumber(clientCarDTO.getPhoneNumber());
        client = clientRepository.save(client);

        Car car = new Car();
        car.setClient(client);
        car.setCarModel(clientCarDTO.getCarModel());
        car.setCarColor(clientCarDTO.getCarColor());
        car.setCarNumberPlate(clientCarDTO.getCarNumberPlate());

        return carRepository.save(car);
    }
}
