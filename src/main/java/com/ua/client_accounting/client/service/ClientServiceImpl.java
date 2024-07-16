package com.ua.client_accounting.client.service;

import com.ua.client_accounting.client.dto.create.CreateClientRequest;
import com.ua.client_accounting.client.dto.create.CreateClientResponse;
import com.ua.client_accounting.client.dto.update.UpdateClientRequest;
import com.ua.client_accounting.client.dto.update.UpdateClientResponse;
import com.ua.client_accounting.client.entity.Client;
import com.ua.client_accounting.client.repository.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class ClientServiceImpl implements ClientService{

    @Autowired
    private final ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public CreateClientResponse createClient(CreateClientRequest request) {
        Client client = new Client();

        client.setName(request.getName());
        client.setPhoneNumber(request.getPhoneNumber());

        clientRepository.save(client);

        CreateClientResponse response = new CreateClientResponse();
        response.setClientId(client.getId());

        return response;
    }

    @Override
    public Client getClientById(UUID id) {
        return clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
    }

    @Override
    public void deleteClient(UUID id) {
        Client client = getClientById(id);
        clientRepository.delete(client);
    }

    @Override
    public UpdateClientResponse updateClient(UUID id, UpdateClientRequest request) {
        Client client = getClientById(id);

        client.setName(request.getName());
        client.setPhoneNumber(request.getPhoneNumber());
        client = clientRepository.save(client);

        UpdateClientResponse response = new UpdateClientResponse();
        response.setClientId(client.getId());
        response.setName(client.getName());
        response.setPhoneNumber(client.getPhoneNumber());
        return response;
    }
}
