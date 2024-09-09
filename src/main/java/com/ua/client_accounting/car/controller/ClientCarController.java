package com.ua.client_accounting.car.controller;

import com.ua.client_accounting.car.dto.ClientCarDTO;
import com.ua.client_accounting.car.service.ClientCarServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/V2")
@AllArgsConstructor
public class ClientCarController {

    private ClientCarServiceImpl clientCarService;

    @GetMapping("/client-car")
    public ResponseEntity<List<ClientCarDTO>> getClientCars() {
        List<ClientCarDTO> clientCarDTOS = clientCarService.getClientCars();
        return ResponseEntity.ok(clientCarDTOS);
    }
}
