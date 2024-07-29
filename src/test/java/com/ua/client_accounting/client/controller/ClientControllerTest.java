package com.ua.client_accounting.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ua.client_accounting.client.dto.create.CreateClientRequest;
import com.ua.client_accounting.client.dto.create.CreateClientResponse;
import com.ua.client_accounting.client.entity.Client;
import com.ua.client_accounting.client.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void getAllClientTest() throws Exception{
        //Arrange
        Client client1 = new Client(UUID.randomUUID(), "Client 1", "123-456-7890");
        Client client2 = new Client(UUID.randomUUID(), "Client 2", "098-765-4321");

        List<Client> clients = Arrays.asList(client1, client2);

        when(clientService.getAllClients()).thenReturn(clients);

        //Convert clients to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String clientsJson = objectMapper.writeValueAsString(clients);

        //Act & Assert
        mockMvc.perform(get("/api/V2/clients")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(clientsJson));

        verify(clientService, times(1)).getAllClients();
    }

    @Test
    void getClient_Success_Test() throws Exception {
        //Arrange
        UUID clientId = UUID.randomUUID();
        Client client = new Client(clientId, "Client", "123-456-7890");

        when(clientService.getClientById(clientId)).thenReturn(client);

        //Convert client to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String clientJson = objectMapper.writeValueAsString(client);

        //Act & Assert
        mockMvc.perform(get("/api/V2/clients/" + clientId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(clientJson));

        verify(clientService, times(1)).getClientById(clientId);
    }
}
