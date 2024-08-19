package com.ua.client_accounting.car.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        //Convert client to JSON
        String carJson = objectMapper.writeValueAsString(carList);

        // Act & Assert
        mockMvc.perform(get("/api/V2/cars")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(carJson));
    }
}
