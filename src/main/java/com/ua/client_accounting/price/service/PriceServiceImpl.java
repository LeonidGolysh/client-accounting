package com.ua.client_accounting.price.service;

import com.ua.client_accounting.price.dto.create.CreateServicePriceRequest;
import com.ua.client_accounting.price.dto.create.CreateServicePriceResponse;
import com.ua.client_accounting.price.dto.update.UpdateServicePriceResponse;
import com.ua.client_accounting.price.entity.ServicePrice;
import com.ua.client_accounting.price.repository.PriceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PriceServiceImpl implements PriceService{
    private final PriceRepository priceRepository;

    @Override
    public List<ServicePrice> getAll() {
        return priceRepository.findAll();
    }

   @Override
    public ServicePrice getPriceById(Long id) {
        return priceRepository.findById(id).orElseThrow(() -> new RuntimeException(id + "Not Found"));
    }

    @Override
    public CreateServicePriceResponse createServicePrice(CreateServicePriceRequest request) {
        ServicePrice servicePrice = new ServicePrice();
        servicePrice.setServiceName(request.getServiceName());
        servicePrice.setPrice(request.getPrice());

        priceRepository.save(servicePrice);
        CreateServicePriceResponse response = new CreateServicePriceResponse();
        response.setId(servicePrice.getId());

        return response;
    }

    @Override
    public UpdateServicePriceResponse updateServicePrice(Long id, CreateServicePriceRequest request) {
        ServicePrice servicePrice = getPriceById(id);
        servicePrice.setServiceName(request.getServiceName());
        servicePrice.setPrice(request.getPrice());

        priceRepository.save(servicePrice);

        UpdateServicePriceResponse response = new UpdateServicePriceResponse();
        response.setId(servicePrice.getId());
        response.setServiceName(servicePrice.getServiceName());
        response.setPrice(servicePrice.getPrice());

        return response;
    }

    @Override
    public void deletePrice(Long id) {
        ServicePrice servicePrice = getPriceById(id);
        priceRepository.delete(servicePrice);
    }
}
