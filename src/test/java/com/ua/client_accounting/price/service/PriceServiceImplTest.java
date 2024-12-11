package com.ua.client_accounting.price.service;

import com.ua.client_accounting.price.dto.create.CreateServicePriceRequest;
import com.ua.client_accounting.price.dto.create.CreateServicePriceResponse;
import com.ua.client_accounting.price.dto.update.UpdateServicePriceRequest;
import com.ua.client_accounting.price.dto.update.UpdateServicePriceResponse;
import com.ua.client_accounting.price.entity.ServicePrice;
import com.ua.client_accounting.price.repository.PriceRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceServiceImplTest {
    static Long serviceId;

    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private PriceServiceImpl priceService;

    final ServicePrice servicePrice = new ServicePrice(serviceId, "Service 1", new BigDecimal(100.00));

    @BeforeAll
    static void setUpBeforeClass() {
        serviceId = 1L;
    }

    @Test
    void getAll_ShouldReturnListOfServicePricesTest() {
        List<ServicePrice> prices = List.of(
                new ServicePrice(1L, "Service 1", new BigDecimal("100.00")),
                new ServicePrice(2L, "Service 2", new BigDecimal("200.00"))
        );
        when(priceRepository.findAll()).thenReturn(prices);

        List<ServicePrice> result = priceService.getAll();

        assertEquals(2, result.size());
        assertEquals("Service 1", result.get(0).getServiceName());
        assertEquals(new BigDecimal("100.00"), result.get(0).getPrice());
        verify(priceRepository, times(1)).findAll();
    }

    @Test
    void getPriceById_Success_Test() {
        when(priceRepository.findById(serviceId)).thenReturn(Optional.of(servicePrice));

        ServicePrice result = priceService.getPriceById(serviceId);

        assertNotNull(result);
        assertEquals(serviceId, result.getId());
        assertEquals("Service 1", result.getServiceName());
        assertEquals(new BigDecimal(100.00), result.getPrice());
        verify(priceRepository, times(1)).findById(serviceId);
    }

    @Test
    void getPriceById_NotFound_Test() {
        when(priceRepository.findById(serviceId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            priceService.getPriceById(serviceId);
                });

        assertEquals("Service with ID " + serviceId + " not found", exception.getMessage());
        verify(priceRepository, times(1)).findById(serviceId);
    }

    @Test
    void createServicePriceTest() {
        CreateServicePriceRequest request = new CreateServicePriceRequest();
        request.setServiceName("Service 1L");
        request.setPrice(new BigDecimal("100.00"));

        when(priceRepository.save(any(ServicePrice.class))).thenAnswer(invocationOnMock -> {
            ServicePrice saveService = invocationOnMock.getArgument(0);
            saveService.setId(serviceId);
            return saveService;
        });

        CreateServicePriceResponse response = priceService.createServicePrice(request);

        assertNotNull(response);
        assertEquals(serviceId, response.getId());
        verify(priceRepository, times(1)).save(any(ServicePrice.class));
    }

    @Test
    void updateServicePrice_Success_Test() {
        UpdateServicePriceRequest request = new UpdateServicePriceRequest();
        request.setServiceName("Update Service 1");
        request.setPrice(new BigDecimal("200.00"));

        when(priceRepository.findById(serviceId)).thenReturn(Optional.of(servicePrice));
        when(priceRepository.save(any(ServicePrice.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        UpdateServicePriceResponse response = priceService.updateServicePrice(serviceId, request);

        assertNotNull(response);
        assertEquals(serviceId, response.getId());
        assertEquals("Update Service 1", response.getServiceName());
        assertEquals(new BigDecimal("200.00"), response.getPrice());

        verify(priceRepository, times(1)).findById(serviceId);
        verify(priceRepository, times(1)).save(servicePrice);
    }

    @Test
    void updateServicePrice_NotFound_Test() {
        UpdateServicePriceRequest request = new UpdateServicePriceRequest();
        request.setServiceName("Update Service 1");
        request.setPrice(new BigDecimal("200.00"));

        when(priceRepository.findById(serviceId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            priceService.updateServicePrice(serviceId, request);
        });

        assertEquals("Service with ID " + serviceId + " not found", exception.getMessage());

        verify(priceRepository, times(1)).findById(serviceId);
        verify(priceRepository, times(0)).save(any(ServicePrice.class));
    }

    @Test
    void deletePrice_Success_Test() {
        when(priceRepository.findById(serviceId)).thenReturn(Optional.of(servicePrice));

        priceService.deletePrice(serviceId);

        verify(priceRepository, times(1)).delete(servicePrice);
    }

    @Test
    void deletePrice_NotFound_Test() {
        when(priceRepository.findById(serviceId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            priceService.deletePrice(serviceId);
        });

        assertEquals("Service with ID " + serviceId + " not found", exception.getMessage());
        verify(priceRepository, times(0)).delete(any(ServicePrice.class));
    }
}
