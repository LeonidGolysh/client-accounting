package com.ua.client_accounting.client.service;

import com.ua.client_accounting.client.entity.Client;
import com.ua.client_accounting.client.repository.ClientRepository;
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

    @InjectMocks
    private ClientServiceImpl clientServiceImpl;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllClientsTest() {
        //Arrange
        Client client1 = new Client(UUID.randomUUID(), "Client 1", "123-456-7890");
        Client client2 = new Client(UUID.randomUUID(), "Client 2", "098-765-4321");

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
        UUID clientId = UUID.randomUUID();
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
        UUID clientId = UUID.randomUUID();

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        //Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            clientServiceImpl.getClientById(clientId);
        });

        assertEquals("Client not found", exception.getMessage());

        verify(clientRepository, times(1)).findById(clientId);
    }
}
