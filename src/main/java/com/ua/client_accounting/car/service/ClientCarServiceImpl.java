package com.ua.client_accounting.car.service;

import com.ua.client_accounting.car.dto.ClientCarDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
