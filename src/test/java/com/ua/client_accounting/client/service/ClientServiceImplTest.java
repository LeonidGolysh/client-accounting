package com.ua.client_accounting.client.service;

import com.ua.client_accounting.client.dto.create.CreateClientRequest;
import com.ua.client_accounting.client.dto.create.CreateClientResponse;
import com.ua.client_accounting.client.dto.update.UpdateClientRequest;
import com.ua.client_accounting.client.dto.update.UpdateClientResponse;
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

class ClientServiceImplTest {

    static UUID clientId;

    @InjectMocks
    private ClientServiceImpl clientServiceImpl;

    @Mock
    private ClientRepository clientRepository;

    @BeforeAll
    static void setUpBeforeClass() {
        clientId = UUID.randomUUID();
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllClientsTest() {
        //Arrange
        Client client1 = new Client(clientId, "Client 1", "123-456-7890");
        Client client2 = new Client(clientId, "Client 2", "098-765-4321");

        List<Client> mockClients = Arrays.asList(client1, client2);
        when(clientRepository.findAll()).thenReturn(mockClients);

        //Act
        List<Client> result = clientServiceImpl.getAllClients();

        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Client 1", result.get(0).getName());
        assertEquals("123-456-7890", result.get(0).getPhoneNumber());
        assertEquals("Client 2", result.get(1).getName());
        assertEquals("098-765-4321", result.get(1).getPhoneNumber());

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void getClientById_Success_Test() {
        //Arrange
        Client client = new Client(clientId, "Client 1", "123-456-7890");

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        //Act
        Client result = clientServiceImpl.getClientById(clientId);

        //Assert
        assertNotNull(result);
        assertEquals(clientId, result.getId());
        assertEquals("Client 1", result.getName());
        assertEquals("123-456-7890", result.getPhoneNumber());

        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    void getClientById_NotFound_Test() {
        //Arrange
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        //Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            clientServiceImpl.getClientById(clientId);
        });

        assertEquals("Client not found", exception.getMessage());

        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    void createClientTest() {
        //Arrange
        CreateClientRequest request = new CreateClientRequest();
        request.setName("New Client");
        request.setPhoneNumber("1234567890");

        Client client = new Client();

        client.setId(clientId);
        client.setName(request.getName());
        client.setPhoneNumber(request.getPhoneNumber());

        when(clientRepository.save(any(Client.class))).thenAnswer(invocationOnMock -> {
            Client savedClient = invocationOnMock.getArgument(0);
            savedClient.setId(clientId);    // Simulate ID generation on save
            return savedClient;
        });

        //Act
        CreateClientResponse response = clientServiceImpl.createClient(request);

        //Assert
        assertNotNull(response);
        assertEquals(clientId, response.getClientId());

        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void deleteClient_Success_Test(){
        //Arrange
        Client client = new Client(clientId, "Client 1", "123-4567890");

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        //Act
        clientServiceImpl.deleteClient(clientId);

        //Assert
        verify(clientRepository, times(1)).delete(client);
    }

    @Test
    void deleteClient_NotFound_Test(){
        //Arrange
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            clientServiceImpl.deleteClient(clientId);
        });

        assertEquals("Client not found", exception.getMessage());

        verify(clientRepository, times(0)).delete(any(Client.class));
    }

    @Test
    void updateClient_Success_Test(){
        //Arrange
        Client existingClient = new Client(clientId, "Exist Client", "1112345890");

        UpdateClientRequest request = new UpdateClientRequest();
        request.setName("Update Client");
        request.setPhoneNumber("1116789890");

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        //Act
        UpdateClientResponse response = clientServiceImpl.updateClient(clientId, request);

        //Assert
        assertNotNull(response);
        assertEquals(clientId, response.getClientId());
        assertEquals("Update Client", response.getName());
        assertEquals("1116789890", response.getPhoneNumber());

        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, times(1)).save(existingClient);
    }

    @Test
    void updateClient_NotFount_Test(){
        //Arrange
        UpdateClientRequest request = new UpdateClientRequest();
        request.setName("Update Client");
        request.setPhoneNumber("1116789890");

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            clientServiceImpl.updateClient(clientId, request);
        });

        assertEquals("Client not found", exception.getMessage());

        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, times(0)).save(any(Client.class));
    }
}
