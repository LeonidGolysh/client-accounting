package com.ua.client_accounting.price.service;

import com.ua.client_accounting.price.dto.create.CreateServicePriceRequest;
import com.ua.client_accounting.price.dto.create.CreateServicePriceResponse;
import com.ua.client_accounting.price.dto.update.UpdateServicePriceResponse;
import com.ua.client_accounting.price.entity.ServicePrice;

import java.util.List;

public interface PriceService {
    List<ServicePrice> getAll();

    ServicePrice getPriceById(Long id);

    CreateServicePriceResponse createServicePrice(CreateServicePriceRequest request);

    UpdateServicePriceResponse updateServicePrice(Long id, CreateServicePriceRequest request);

    void deletePrice(Long id);
}
