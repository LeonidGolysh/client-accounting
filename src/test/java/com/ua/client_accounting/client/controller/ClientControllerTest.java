package com.ua.client_accounting.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ua.client_accounting.client.dto.create.CreateClientRequest;
import com.ua.client_accounting.client.dto.create.CreateClientResponse;
import com.ua.client_accounting.client.dto.update.UpdateClientRequest;
import com.ua.client_accounting.client.dto.update.UpdateClientResponse;
import com.ua.client_accounting.client.entity.Client;
import com.ua.client_accounting.client.service.ClientService;
import org.junit.jupiter.api.BeforeAll;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class ClientControllerTest {

    static UUID clientId;

    @Mock
    ClientService clientService;

    @InjectMocks
    ClientController clientController;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUpBeforeClass() {
        clientId = UUID.randomUUID();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    void createClientTest() throws Exception {
        //Arrange
        CreateClientRequest request = new CreateClientRequest();
        request.setName("New Client");
        request.setPhoneNumber("123-4567");

        CreateClientResponse response = new CreateClientResponse();
        response.setClientId(clientId);

        when(clientService.createClient(any(CreateClientRequest.class))).thenReturn(response);

        //Convert request to JSON
        String requestJson = objectMapper.writeValueAsString(request);

        //Act & Assert
        mockMvc.perform(post("/api/V2/clients/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.clientId").value(clientId.toString()));

        verify(clientService, times(1)).createClient(any(CreateClientRequest.class));
    }

    @Test
    void getAllClientTest() throws Exception{
        //Arrange
        Client client1 = new Client(clientId, "Client 1", "123-456-7890");
        Client client2 = new Client(clientId, "Client 2", "098-765-4321");

        List<Client> clients = Arrays.asList(client1, client2);

        when(clientService.getAllClients()).thenReturn(clients);

        //Convert clients to JSON
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
        Client client = new Client(clientId, "Client", "123-456-7890");

        when(clientService.getClientById(clientId)).thenReturn(client);

        //Convert client to JSON
        String clientJson = objectMapper.writeValueAsString(client);

        //Act & Assert
        mockMvc.perform(get("/api/V2/clients/" + clientId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(clientJson));

        verify(clientService, times(1)).getClientById(clientId);
    }

    @Test
    void deleteClient_Success_Test() throws Exception {
        //Arrange
        doNothing().when(clientService).deleteClient(clientId);

        //Act & Assert
        mockMvc.perform(delete("/api/V2/clients/delete/" + clientId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(clientService, times(1)).deleteClient(clientId);
    }

    @Test
    void updateClientTest() throws Exception{
        //Arrange
        UpdateClientRequest request = new UpdateClientRequest();
        request.setName("Update Client");
        request.setPhoneNumber("111-6789");

        UpdateClientResponse response = new UpdateClientResponse();
        response.setClientId(clientId);
        response.setName("Update Client");
        response.setPhoneNumber("111-6789");

        when(clientService.updateClient(eq(clientId), any(UpdateClientRequest.class))).thenReturn(response);

        //Convert request & response to JSON
        String requestJson = objectMapper.writeValueAsString(request);
        String responseJson = objectMapper.writeValueAsString(response);

        //Act & Assert
        mockMvc.perform(put("/api/V2/clients/edit/" + clientId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson));

        verify(clientService,times(1)).updateClient(eq(clientId), any(UpdateClientRequest.class));
    }
}
