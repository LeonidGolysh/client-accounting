package com.ua.client_accounting.car.service;

import com.ua.client_accounting.car.entity.Car;
import com.ua.client_accounting.car.repository.CarRepository;
import com.ua.client_accounting.client.entity.Client;
import com.ua.client_accounting.client.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CarServiceImplTest {

    @InjectMocks
    CarServiceImpl carService;

    @Mock
    CarRepository carRepository;

    @Mock
    ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllCarsTest() {
        //Arrange
        Client client1 = new Client(UUID.randomUUID(), "Client 1", "1234567890");
        Client client2 = new Client(UUID.randomUUID(), "Client 2", "0987654321");

        Car car1 = new Car(UUID.randomUUID(), client1, "BMW", "Red", "XY1234YZ");
        Car car2 = new Car(UUID.randomUUID(), client2, "Honda", "Blue", "AB4444BC");

        List<Car> cars = Arrays.asList(car1, car2);

        when(carRepository.findAll()).thenReturn(cars);

        //Act
        List<Car> result = carService.getAllCars();

        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("BMW", result.get(0).getCarModel());
        assertEquals("Honda", result.get(1).getCarModel());
        assertEquals("Red", result.get(0).getCarColor());
        assertEquals("Blue", result.get(1).getCarColor());
        assertEquals("XY1234YZ", result.get(0).getCarNumberPlate());
        assertEquals("AB4444BC", result.get(1).getCarNumberPlate());
        assertEquals("Client 1", result.get(0).getClient().getName());
        assertEquals("Client 2", result.get(1).getClient().getName());
        assertEquals("1234567890", result.get(0).getClient().getPhoneNumber());
        assertEquals("0987654321", result.get(1).getClient().getPhoneNumber());

        verify(carRepository, times(1)).findAll();
    }
}
