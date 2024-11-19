package com.ua.client_accounting.car.repository;

import com.ua.client_accounting.car.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {
    List<Car> findByClientId(UUID clientId);
    Optional<Car> findByCarNumberPlate(String carNumberPlate);
}
