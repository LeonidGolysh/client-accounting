package com.ua.client_accounting.client.controller;

import com.ua.client_accounting.client.dto.create.CreateClientRequest;
import com.ua.client_accounting.client.dto.update.UpdateClientRequest;
import com.ua.client_accounting.client.entity.Client;
import com.ua.client_accounting.client.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@AllArgsConstructor
public class ClientController {

    @Autowired
    private final ClientService clientService;

    @GetMapping("/create")
    public String createClient(Model model) {
        model.addAttribute("clientEntity", new CreateClientRequest());
        return "clients/create/create-client";
    }

    @PostMapping("/create")
    public String createClient(@ModelAttribute("clientEntity") CreateClientRequest request, Model model) {
        clientService.createClient(request);
        return "redirect:/client";
    }

    @GetMapping("/client")
    public String showClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "clients/client";
    }

    @GetMapping("{id}")
    public Client getClient(@PathVariable UUID id) {
        return clientService.getClientById(id);
    }

    @PostMapping("/delete/{id}")
    public String deleteClient(@PathVariable UUID id) {
        clientService.deleteClient(id);
        return "redirect:/client";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Client client = clientService.getClientById(id);
        model.addAttribute("client", client);
        return "clients/edit/edit-client";
    }

    @PostMapping("/edit/{id}")
    public String updateClient(@PathVariable UUID id, @ModelAttribute UpdateClientRequest updateClientRequest) {
        clientService.updateClient(id, updateClientRequest);
        return "redirect:/client";
    }
}
