package com.ua.client_accounting.client.controller;

import com.ua.client_accounting.client.dto.create.CreateClientRequest;
import com.ua.client_accounting.client.service.ClientService;
import com.ua.client_accounting.client.service.ClientServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class MainController {
    private final ClientServiceImpl clientServiceImpl;

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("create")
    public String createClient(Model model) {
        model.addAttribute("clientEntity", new CreateClientRequest());
        return "createClient";
    }

    @PostMapping("/create")
    public String createClient(@ModelAttribute("clientEntity") CreateClientRequest request, Model model) {
        clientServiceImpl.createClient(request);
        return "redirect:/client";
    }

    @GetMapping("/client")
    public String showClients(Model model) {
        model.addAttribute("clients", clientServiceImpl.getAllClients());
        return "client";
    }
}
