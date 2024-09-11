package com.ua.client_accounting.car.controller;

import com.ua.client_accounting.car.dto.ClientCarDTO;
import com.ua.client_accounting.car.entity.Car;
import com.ua.client_accounting.car.service.ClientCarServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/V2/client-car")
@AllArgsConstructor
public class ClientCarController {

    private ClientCarServiceImpl clientCarService;

    @GetMapping
    public ResponseEntity<List<ClientCarDTO>> getClientCars() {
        List<ClientCarDTO> clientCarDTOS = clientCarService.getClientCars();
        return ResponseEntity.ok(clientCarDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientCarDTO> getClientCarById(@PathVariable("id") UUID carId) {
        ClientCarDTO clientCarDTO = clientCarService.getClientCarById(carId);
        if (clientCarDTO != null) {
            return ResponseEntity.ok(clientCarDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/model/{carModel}")
    public ResponseEntity<List<ClientCarDTO>> getClientCarsByModel(@PathVariable("carModel") String carModel) {
        List<ClientCarDTO> clientCarDTOs = clientCarService.getClientCarByModel(carModel);
        if (!clientCarDTOs.isEmpty()) {
            return ResponseEntity.ok(clientCarDTOs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Car> createCarWithClient(@RequestBody ClientCarDTO clientCarDTO) {
        Car car = clientCarService.createCarWithClient(clientCarDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(car);
    }
}
