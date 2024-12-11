package com.ua.client_accounting.price.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ua.client_accounting.price.dto.create.CreateServicePriceRequest;
import com.ua.client_accounting.price.dto.create.CreateServicePriceResponse;
import com.ua.client_accounting.price.dto.update.UpdateServicePriceRequest;
import com.ua.client_accounting.price.dto.update.UpdateServicePriceResponse;
import com.ua.client_accounting.price.entity.ServicePrice;
import com.ua.client_accounting.price.service.PriceService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PriceServiceController.class)
class PriceServiceControllerTest {
    static Long serviceId;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PriceService priceService;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    static void setUpBeforeClass() {
        serviceId = 1L;
    }

    @Test
    void getAllPricesTest() throws Exception {
        List<ServicePrice> mockPrice = List.of(
                new ServicePrice(serviceId, "Service 1", new BigDecimal("100.00")),
                new ServicePrice(2L, "Service 2", new BigDecimal("200.00"))
        );

        when(priceService.getAll()).thenReturn(mockPrice);

        String expectedJson = objectMapper.writeValueAsString(mockPrice);

        mockMvc.perform(get("/api/clients-accounting/price-service")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(priceService, times(1)).getAll();
    }

    @Test
    void getPriceServiceTest() throws Exception {
        ServicePrice servicePrice = new ServicePrice(serviceId, "Service 1", new BigDecimal("100.00"));

        when(priceService.getPriceById(serviceId)).thenReturn(servicePrice);

        String expectedJson = objectMapper.writeValueAsString(servicePrice);

        mockMvc.perform(get("/api/clients-accounting/price-service/" + serviceId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andReturn();

        verify(priceService, times(1)).getPriceById(serviceId);
    }

    @Test
    void createPriceServiceTest() throws Exception {
        CreateServicePriceRequest request = new CreateServicePriceRequest();
        request.setServiceName("Service 1");
        request.setPrice(new BigDecimal("100.00"));

        CreateServicePriceResponse response = new CreateServicePriceResponse();
        response.setId(serviceId);

        when(priceService.createServicePrice(any(CreateServicePriceRequest.class))).thenReturn(response);

        String expectedJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/clients-accounting/price-service/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedJson))
                .andExpect(status().isOk())
                .andReturn();

        verify(priceService, times(1)).createServicePrice(any(CreateServicePriceRequest.class));
    }

    @Test
    void updatePriceServiceTest() throws Exception {
        UpdateServicePriceRequest request = new UpdateServicePriceRequest();
        request.setServiceName("Service 1");
        request.setPrice(new BigDecimal("100.00"));

        UpdateServicePriceResponse response = new UpdateServicePriceResponse();
        response.setId(serviceId);
        response.setServiceName("Service 1");
        response.setPrice(new BigDecimal("100.00"));

        when(priceService.updateServicePrice(eq(serviceId), any(UpdateServicePriceRequest.class))).thenReturn(response);

        String requestJson = objectMapper.writeValueAsString(request);
        String responseJson = objectMapper.writeValueAsString(response);

        mockMvc.perform(put("/api/clients-accounting/price-service/edit/" + serviceId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andReturn();

        verify(priceService, times(1)).updateServicePrice(eq(serviceId), any(UpdateServicePriceRequest.class));
    }

    @Test
    void deletePriceServiceTest() throws Exception {
        doNothing().when(priceService).deletePrice(serviceId);

        mockMvc.perform(delete("/api/clients-accounting/price-service/delete/" + serviceId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(priceService, times(1)).deletePrice(serviceId);
    }
}
