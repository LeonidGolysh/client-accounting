package com.ua.client_accounting.client.controller;

import com.ua.client_accounting.client.dto.create.CreateClientRequest;
import com.ua.client_accounting.client.entity.Client;
import com.ua.client_accounting.client.service.ClientService;
import com.ua.client_accounting.client.service.ClientServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@AllArgsConstructor
public class MainController {
    private final ClientService clientService;

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("/create")
    public String createClient(Model model) {
        model.addAttribute("clientEntity", new CreateClientRequest());
        return "createClient";
    }

    @PostMapping("/create")
    public String createClient(@ModelAttribute("clientEntity") CreateClientRequest request, Model model) {
        clientService.createClient(request);
        return "redirect:/client";
    }

    @GetMapping("/client")
    public String showClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "client";
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
}
