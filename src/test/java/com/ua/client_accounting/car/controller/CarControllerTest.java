package com.ua.client_accounting.car.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ua.client_accounting.car.dto.create.CreateCarRequest;
import com.ua.client_accounting.car.dto.create.CreateCarResponse;
import com.ua.client_accounting.car.dto.update.UpdateCarRequest;
import com.ua.client_accounting.car.dto.update.UpdateCarResponse;
import com.ua.client_accounting.car.entity.Car;
import com.ua.client_accounting.car.service.CarService;
import com.ua.client_accounting.client.entity.Client;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CarControllerTest {

    static UUID clientId;
    static UUID carId;

    @Mock
    CarService carService;

    @InjectMocks
    CarController carController;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUpBeforeClass() {
        clientId = UUID.randomUUID();
        carId = UUID.randomUUID();
    }
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
    }

    @Test
    void getAllCarsTest () throws Exception {
        // Arrange
        Client client = new Client(clientId, "Bob", "1234567890");
        Car car = new Car(carId, client, "BMW", "Red", "ABC123");
        List<Car> carList = Collections.singletonList(car);

        when(carService.getAllCars()).thenReturn(carList);

        //Convert car to JSON
        String carJson = objectMapper.writeValueAsString(carList);

        // Act & Assert
        mockMvc.perform(get("/api/V2/cars")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(carJson));

        verify(carService, times(1)).getAllCars();
    }

    @Test
    void getCarByIdTest() throws Exception {
        //Arrange
        Client client = new Client(clientId, "Bob", "1234567890");
        Car car = new Car(carId, client, "BMW", "Red", "ABC123");

        when(carService.getCarById(carId)).thenReturn(car);

        //Convert car to JSON
        String carJson = objectMapper.writeValueAsString(car);

        //Act & Assert
        mockMvc.perform(get("/api/V2/cars/" + carId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(carJson))
                .andReturn();

        verify(carService, times(1)).getCarById(carId);
    }

    @Test
    void createCarTest() throws Exception {
        //Assert
        CreateCarRequest request = new CreateCarRequest();
        request.setClientId(clientId);
        request.setCarModel("BMW");
        request.setCarColor("Red");
        request.setCarNumberPlate("ABC123");

        CreateCarResponse response = new CreateCarResponse();
        response.setCarId(carId);

        when(carService.createCar(any(CreateCarRequest.class))).thenReturn(response);

        //Convert request to JSON
        String requestJson = objectMapper.writeValueAsString(request);

        //Act & Assert
        mockMvc.perform(post("/api/V2/cars/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId").value(carId.toString()));

        verify(carService, times(1)).createCar(any(CreateCarRequest.class));
    }

    @Test
    void deleteCarTest() throws Exception {
        //Arrange
        doNothing().when(carService).deleteCar(carId);

        //Act & Assert
        mockMvc.perform(delete("/api/V2/cars/delete/" + carId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(carService, times(1)).deleteCar(carId);
    }

    @Test
    void updateCarTest() throws Exception {
        //Arrange
        UpdateCarRequest request = new UpdateCarRequest();
        request.setClientId(clientId);
        request.setCarModel("BMW");
        request.setCarColor("Red");
        request.setCarNumberPlate("ABC123");

        UpdateCarResponse response = new UpdateCarResponse();
        response.setCarId(carId);
        response.setClientId(clientId);
        response.setCarModel("BMW");
        response.setCarColor("Red");
        response.setCarNumberPlate("ABC123");

        when(carService.updateCar(eq(carId), any(UpdateCarRequest.class))).thenReturn(response);

        //Convert request & response to JSON
        String requestJson = objectMapper.writeValueAsString(request);
        String responseJson = objectMapper.writeValueAsString(response);

        //Act & Assert
        mockMvc.perform(put("/api/V2/cars/edit/" + carId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));

        verify(carService, times(1)).updateCar(eq(carId), any(UpdateCarRequest.class));
    }
}
