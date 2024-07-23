package com.ua.client_accounting.client.controller;

import com.ua.client_accounting.client.dto.create.CreateClientRequest;
import com.ua.client_accounting.client.dto.create.CreateClientResponse;
import com.ua.client_accounting.client.dto.update.UpdateClientRequest;
import com.ua.client_accounting.client.dto.update.UpdateClientResponse;
import com.ua.client_accounting.client.entity.Client;
import com.ua.client_accounting.client.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/V2/clients")
@AllArgsConstructor
public class ClientController {

    @Autowired
    private final ClientService clientService;

    @PostMapping("/create")
    public ResponseEntity<CreateClientResponse> createClient(@RequestBody CreateClientRequest request) {
        CreateClientResponse response = clientService.createClient(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClient(@PathVariable UUID id) {
        Client clients = clientService.getClientById(id);
        return ResponseEntity.ok(clients);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<UpdateClientResponse> updateClient(@PathVariable UUID id, @RequestBody UpdateClientRequest request) {
        UpdateClientResponse response = clientService.updateClient(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
