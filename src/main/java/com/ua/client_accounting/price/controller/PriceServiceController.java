package com.ua.client_accounting.price.controller;

import com.ua.client_accounting.price.dto.create.CreateServicePriceRequest;
import com.ua.client_accounting.price.dto.create.CreateServicePriceResponse;
import com.ua.client_accounting.price.dto.update.UpdateServicePriceRequest;
import com.ua.client_accounting.price.dto.update.UpdateServicePriceResponse;
import com.ua.client_accounting.price.entity.ServicePrice;
import com.ua.client_accounting.price.service.PriceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/V2/price-service")
@AllArgsConstructor
public class PriceServiceController {

    @Autowired
    private final PriceService priceService;

    @GetMapping
    public ResponseEntity<List<ServicePrice>> getAllPrices() {
        List<ServicePrice> servicePrice = priceService.getAll();
        return ResponseEntity.ok(servicePrice);
    }

    @PostMapping("/create")
    public ResponseEntity<CreateServicePriceResponse> createPriceService(@RequestBody CreateServicePriceRequest request) {
        CreateServicePriceResponse response = priceService.createServicePrice(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicePrice> getPriceService(@PathVariable Long id) {
        ServicePrice servicePrice = priceService.getPriceById(id);
        return ResponseEntity.ok(servicePrice);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<UpdateServicePriceResponse> updatePriceService(@PathVariable Long id,@RequestBody UpdateServicePriceRequest request) {
        UpdateServicePriceResponse response = priceService.updateServicePrice(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePriceService(@PathVariable Long id) {
        priceService.deletePrice(id);
        return ResponseEntity.noContent().build();
    }
}
