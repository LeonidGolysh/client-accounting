package com.ua.client_accounting.car.service;

import com.ua.client_accounting.car.dto.ClientCarDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientCarServiceImpl {

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
}
