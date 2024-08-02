package com.ua.client_accounting.car.service;

import com.ua.client_accounting.car.dto.create.CreateCarRequest;
import com.ua.client_accounting.car.dto.create.CreateCarResponse;
import com.ua.client_accounting.car.dto.update.UpdateCarRequest;
import com.ua.client_accounting.car.dto.update.UpdateCarResponse;
import com.ua.client_accounting.car.entity.Car;
import com.ua.client_accounting.car.repository.CarRepository;
import com.ua.client_accounting.client.entity.Client;
import com.ua.client_accounting.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService{

    private final CarRepository carRepository;
    private final ClientRepository clientRepository;

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public CreateCarResponse createCar(CreateCarRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client Not Found"));

        Car car = new Car();

        car.setClient(client);
        car.setCarModel(request.getCarModel());
        car.setCarColor(request.getCarColor());
        car.setCarNumberPlate(request.getCarNumberPlate());

        carRepository.save(car);

        CreateCarResponse response = new CreateCarResponse();
        response.setCarId(car.getId());

        return response;
    }

    @Override
    public Car getCarById(UUID id) {
        return carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car Not Found"));
    }

    @Override
    public void deleteCar(UUID id) {
        Car car = getCarById(id);
        carRepository.delete(car);
    }

    @Override
    public UpdateCarResponse updateCar(UUID carId, UpdateCarRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client Not Found"));

        Car car = getCarById(carId);

        car.setClient(client);
        car.setCarModel(request.getCarModel());
        car.setCarColor(request.getCarColor());
        car.setCarNumberPlate(request.getCarNumberPlate());

        car = carRepository.save(car);

        UpdateCarResponse response = new UpdateCarResponse();

        response.setCarId(car.getId());
        response.setClientId(car.getClient().getId());
        response.setCarModel(car.getCarModel());
        response.setCarColor(car.getCarColor());
        response.setCarNumberPlate(car.getCarNumberPlate());

        return response;
    }
}