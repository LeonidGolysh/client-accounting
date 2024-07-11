package com.ua.client_accounting.client.service;

import com.ua.client_accounting.client.dto.create.CreateClientResponse;

public interface ClientService {
    CreateClientResponse createClient(CreateClientResponse createClientResponse);
}
