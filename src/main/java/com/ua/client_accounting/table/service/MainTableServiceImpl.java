package com.ua.client_accounting.table.service;

import com.ua.client_accounting.table.dto.MainTableDTO;

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
public class MainTableServiceImpl implements MainTableService{

    private final ClientRepository clientRepository;
    private final CarRepository carRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<MainTableDTO> getClientCars() {
        Query query = entityManager.createQuery(
                "SELECT new com.ua.client_accounting.table.dto.MainTableDTO(" +
                        "car.id, client.name, client.phoneNumber, " +
                        "car.carModel, car.carColor, car.carNumberPlate) " +
                        "FROM Car car JOIN car.client client"
        );
        return query.getResultList();
    }

    public MainTableDTO getClientCarById(UUID carId) {
        Query query = entityManager.createQuery(
                "SELECT new com.ua.client_accounting.table.dto.MainTableDTO(" +
                        "car.id, client.name, client.phoneNumber, " +
                        "car.carModel, car.carColor, car.carNumberPlate) " +
                        "FROM Car car JOIN car.client client " +
                        "WHERE car.id = :carId"
        );
        query.setParameter("carId", carId);
        return (MainTableDTO) query.getSingleResult();
    }

    public List<MainTableDTO> getClientCarByModel(String carModel) {
        Query query = entityManager.createQuery(
                "SELECT new com.ua.client_accounting.table.dto.MainTableDTO(" +
                        "car.id, client.name, client.phoneNumber, " +
                        "car.carModel, car.carColor, car.carNumberPlate) " +
                        "FROM Car car JOIN car.client client " +
                        "WHERE car.carModel = :carModel"
        );
        query.setParameter("carModel", carModel);
        return query.getResultList();
    }

    @Transactional
    public Car createCarWithClient(MainTableDTO mainTableDTO) {
        Client client = new Client();
        client.setName(mainTableDTO.getClientName());
        client.setPhoneNumber(mainTableDTO.getPhoneNumber());
        client = clientRepository.save(client);

        Car car = new Car();
        car.setClient(client);
        car.setCarModel(mainTableDTO.getCarModel());
        car.setCarColor(mainTableDTO.getCarColor());
        car.setCarNumberPlate(mainTableDTO.getCarNumberPlate());

        return carRepository.save(car);
    }
}
