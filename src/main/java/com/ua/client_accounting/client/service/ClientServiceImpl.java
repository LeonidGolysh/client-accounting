package com.ua.client_accounting.client.service;

import com.ua.client_accounting.client.dto.create.CreateClientRequest;
import com.ua.client_accounting.client.dto.create.CreateClientResponse;
import com.ua.client_accounting.client.dto.update.UpdateClientRequest;
import com.ua.client_accounting.client.dto.update.UpdateClientResponse;
import com.ua.client_accounting.client.entity.Client;
import com.ua.client_accounting.client.repository.ClientRepository;
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
        String phoneNumber = request.getPhoneNumber();

        if (!isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Phone number must be either or exactly 10 digits long.");
        }

        client.setPhoneNumber(phoneNumber);

        clientRepository.save(client);

        CreateClientResponse response = new CreateClientResponse();
        response.setClientId(client.getId());

        return response;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber == null || phoneNumber.matches("\\d{10}") || phoneNumber.isEmpty();
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
        String phoneNumber = request.getPhoneNumber();

        if (!isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Phone number must be either or exactly 9 digits long.");
        }

        client.setPhoneNumber(phoneNumber);
        client = clientRepository.save(client);

        UpdateClientResponse response = new UpdateClientResponse();

        response.setClientId(client.getId());
        response.setName(client.getName());
        response.setPhoneNumber(client.getPhoneNumber());

        return response;
    }
}
