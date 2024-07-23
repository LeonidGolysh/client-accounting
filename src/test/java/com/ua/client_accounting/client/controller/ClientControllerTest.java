package com.ua.client_accounting.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ua.client_accounting.client.dto.create.CreateClientRequest;
import com.ua.client_accounting.client.dto.create.CreateClientResponse;
import com.ua.client_accounting.client.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    void createClientTest() throws Exception {
        //Arrange
        CreateClientRequest request = new CreateClientRequest();
        request.setName("New Client");
        request.setPhoneNumber("123-4567");

        UUID generateId = UUID.randomUUID();
        CreateClientResponse response = new CreateClientResponse();
        response.setClientId(generateId);

        when(clientService.createClient(any(CreateClientRequest.class))).thenReturn(response);

        //Convert request to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        //Act & Assert
        mockMvc.perform(post("/api/V2/clients/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.clientId").value(generateId.toString()));

        verify(clientService, times(1)).createClient(any(CreateClientRequest.class));
    }
}
