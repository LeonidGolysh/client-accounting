package com.ua.client_accounting.table.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.Main;
import com.ua.client_accounting.table.dto.MainTableDTO;
import com.ua.client_accounting.table.service.MainTableService;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainTableController.class)
class MainTableControllerTest {
    static UUID orderId;

    static LocalDateTime orderDate;

    static String carModel;
    @Autowired
    MockMvc mockMvc;

    @MockBean
    MainTableService mainTableService;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    static void setUpBeforeClass() {
        orderId = UUID.randomUUID();
        orderDate = LocalDateTime.now();
        carModel = "BMW";
    }

    @Test
    void getAllOrdersTest() throws Exception {
        List<MainTableDTO> mockOrders = List.of(createOrders());

        when(mainTableService.getAllOrders()).thenReturn(mockOrders);

        String expectedJson = objectMapper.writeValueAsString(mockOrders);

        mockMvc.perform(get("/api/clients-accounting")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(mainTableService, times(1)).getAllOrders();
    }

    @Test
    void getAllOrder_noOrders_EmptyListTest() throws Exception {
        when(mainTableService.getAllOrders()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/clients-accounting")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(mainTableService, times(1)).getAllOrders();
    }

    private MainTableDTO createOrders() {
        return MainTableDTO.builder()
                .orderId(orderId)
                .clientName("John Doe")
                .phoneNumber("1234567890")
                .carModel("BMW")
                .carColor("Black")
                .carNumberPlate("ABC123")
                .services(List.of(1L, 2L))
                .orderDate(orderDate)
                .totalPrice(new BigDecimal("100.00"))
                .build();
    }

    @Test
    void getOrderByIdTest() throws Exception {
        MainTableDTO mockOrders = createOrders();

        when(mainTableService.getOrderById(orderId)).thenReturn(mockOrders);

        String expectedJson = objectMapper.writeValueAsString(mockOrders);

        mockMvc.perform(get("/api/clients-accounting/" + orderId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andReturn();

        verify(mainTableService, times(1)).getOrderById(orderId);
    }

    @Test
    void getOrderByCarModelTest() throws Exception {
        List<MainTableDTO> mockOrders = List.of(createOrders());

        when(mainTableService.getOrderCarByModel(carModel)).thenReturn(mockOrders);

        String expectedJson = objectMapper.writeValueAsString(mockOrders);

        mockMvc.perform(get("/api/clients-accounting/model/" + carModel)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andReturn();

        verify(mainTableService, times(1)).getOrderCarByModel(carModel);
    }

    @Test
    void createOrderTest() throws Exception {
        MainTableDTO requestDto = new MainTableDTO();
        requestDto.setClientName("Bob");
        requestDto.setPhoneNumber("1234567890");
        requestDto.setCarModel("BMW");
        requestDto.setCarColor("Red");
        requestDto.setCarNumberPlate("ABC123");
        requestDto.setServices(List.of(1L, 2L));
        requestDto.setOrderDate(orderDate);

        MainTableDTO responseDto = createOrders();

        when(mainTableService.createOrder(any(MainTableDTO.class))).thenReturn(responseDto);

        String expectedJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/clients-accounting/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedJson))
                .andExpect(status().isCreated())
                .andReturn();

        verify(mainTableService, times(1)).createOrder(any(MainTableDTO.class));
    }

    @Test
    void updateOrderTest() throws Exception {
        MainTableDTO requestDto = new MainTableDTO();
        requestDto.setClientName("Bob");
        requestDto.setPhoneNumber("1234567890");
        requestDto.setCarModel("BMW");
        requestDto.setCarColor("Red");
        requestDto.setCarNumberPlate("ABC123");
        requestDto.setServices(List.of(1L, 2L));
        requestDto.setOrderDate(orderDate);

        MainTableDTO responseDto = createOrders();

        when(mainTableService.updateOrder(eq(orderId), any(MainTableDTO.class))).thenReturn(responseDto);

        String expectedJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/api/clients-accounting/edit/" + orderId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedJson))
                .andExpect(status().isOk());
    }

    @Test
    void deleteOrderTest() throws Exception {
        mockMvc.perform(delete("/api/clients-accounting/delete/" + orderId.toString()))
                .andExpect(status().isNoContent());

        verify(mainTableService, times(1)).deleteOrder(orderId);
    }

    @Test
    void deleteOrder_notFoundTest() throws Exception {
        doThrow(new EntityNotFoundException("Order not found")).when(mainTableService).deleteOrder(orderId);

        mockMvc.perform(delete("/api/clients-accounting/delete/" + orderId.toString()))
                .andExpect(status().isNotFound());

        verify(mainTableService, times(1)).deleteOrder(orderId);
    }
}
