package com.ua.client_accounting.client.service;

import com.ua.client_accounting.client.dto.create.CreateClientRequest;
import com.ua.client_accounting.client.dto.create.CreateClientResponse;
import com.ua.client_accounting.client.entity.Client;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    List<Client> getAllClients();
    CreateClientResponse createClient(CreateClientRequest request);
    Client getClientById(UUID id);
    void deleteClient(UUID id);
}
