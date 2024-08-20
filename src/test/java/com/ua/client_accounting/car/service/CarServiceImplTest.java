package com.ua.client_accounting.car.service;

import com.ua.client_accounting.car.dto.create.CreateCarRequest;
import com.ua.client_accounting.car.dto.create.CreateCarResponse;
import com.ua.client_accounting.car.dto.update.UpdateCarRequest;
import com.ua.client_accounting.car.dto.update.UpdateCarResponse;
import com.ua.client_accounting.car.entity.Car;
import com.ua.client_accounting.car.repository.CarRepository;
import com.ua.client_accounting.client.entity.Client;
import com.ua.client_accounting.client.repository.ClientRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceImplTest {
    static UUID clientId;
    static UUID carId;

    @InjectMocks
    CarServiceImpl carService;

    @Mock
    CarRepository carRepository;

    @Mock
    ClientRepository clientRepository;

    @BeforeAll
    static void setUpBeforeClass() {
        clientId = UUID.randomUUID();
        carId = UUID.randomUUID();
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllCarsTest() {
        //Arrange
        Client client1 = new Client(clientId, "Client 1", "1234567890");
        Client client2 = new Client(clientId, "Client 2", "0987654321");

        Car car1 = new Car(carId, client1, "BMW", "Red", "XY1234YZ");
        Car car2 = new Car(carId, client2, "Honda", "Blue", "AB4444BC");

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

    @Test
    void getCarById_Success_Test() {
        //Assert
        Client client = new Client(clientId, "Client", "1234567890");
        Car car = new Car(carId, client, "BMW", "Red", "ABC1234");

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        //Act
        Car result = carService.getCarById(carId);

        //Assert
        assertNotNull(result);
        assertEquals(carId, result.getId());
        assertEquals("Client", result.getClient().getName());
        assertEquals("1234567890", result.getClient().getPhoneNumber());
        assertEquals("BMW", result.getCarModel());
        assertEquals("Red", result.getCarColor());
        assertEquals("ABC1234", result.getCarNumberPlate());

        verify(carRepository, times(1)).findById(carId);
    }

    @Test
    void getCarById_NotFound_Test() {
        //Assert
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        //Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
           carService.getCarById(carId);
        });

//        assertEquals("Car Not Found", exception.getMessage());
        assertEquals("Car with " + carId + " not found", exception.getMessage());
        verify(carRepository, times(1)).findById(carId);
    }

    @Test
    void createCarTest() {
        //Assert
        Client client = new Client(clientId, "Client", "1234567890");

        CreateCarRequest request = new CreateCarRequest();
        request.setClientId(clientId);
        request.setCarModel("BMW");
        request.setCarColor("Red");
        request.setCarNumberPlate("ABC1234");

        Car car = new Car();
        car.setId(carId);
        car.setClient(client);
        car.setCarModel(request.getCarModel());
        car.setCarColor(request.getCarColor());
        car.setCarNumberPlate(request.getCarNumberPlate());

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(carRepository.save(any(Car.class))).thenAnswer(invocationOnMock -> {
           Car saveCar = invocationOnMock.getArgument(0);
           saveCar.setId(carId);
           return saveCar;
        });

        //Act
        CreateCarResponse response = carService.createCar(request);

        //Assert
        assertNotNull(response);
        assertEquals(carId, response.getCarId());
        verify(clientRepository, times(1)).findById(clientId);
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void createCar_ClientNotFound_Test() {
        //Assert
        CreateCarRequest request = new CreateCarRequest();
        request.setClientId(clientId);
        request.setCarModel("BMW");
        request.setCarColor("Red");
        request.setCarNumberPlate("ABC1234");

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> carService.createCar(request));
        verify(clientRepository, times(1)).findById(clientId);
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void deleteCar_Success_Test() {
        //Arrange
        Client client = new Client(clientId, "Bob", "1234567890");
        Car car = new Car(carId, client, "BMW", "Red", "ABC123");

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        //Act
        carService.deleteCar(carId);

        //Assert
        verify(carRepository, times(1)).delete(car);
    }

    @Test
    void deleteClient_NotFound_Test() {
        //Arrange
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        //Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
           carService.deleteCar(carId);
        });

        assertEquals("Car with " + carId + " not found", exception.getMessage());
        verify(carRepository, times(0)).delete(any(Car.class));
    }

    @Test
    void updateCar_Success_Test() {
        //Arrange
        Client client = new Client(clientId, "Bob", "1234567890");
        Car existingCar = new Car(carId, client, "BMW", "Red", "ABC123");

        UpdateCarRequest request = new UpdateCarRequest();
        request.setClientId(clientId);
        request.setCarModel("Update Honda");
        request.setCarColor("Blue");
        request.setCarNumberPlate("DFG123");

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        when(carRepository.save(any(Car.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        //Act
        UpdateCarResponse response = carService.updateCar(carId, request);

        //Assert
        assertNotNull(response);
        assertEquals(carId, response.getCarId());
        assertEquals(clientId, response.getClientId());
        assertEquals("Update Honda", response.getCarModel());
        assertEquals("Blue", response.getCarColor());
        assertEquals("DFG123", response.getCarNumberPlate());

        verify(carRepository, times(1)).findById(carId);
        verify(carRepository, times(1)).save(existingCar);
    }

    @Test
    void updateCar_NotFound_Test() {
        //Arrange
        UpdateCarRequest request = new UpdateCarRequest();
        request.setClientId(clientId);
        request.setCarModel("Update Honda");
        request.setCarColor("Blue");
        request.setCarNumberPlate("DFG123");

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        //Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
           carService.updateCar(carId, request);
        });

        assertEquals("Car Not Found", exception.getMessage());

        verify(carRepository, times(1)).findById(carId);
        verify(carRepository, times(0)).save(any(Car.class));
    }
}
