package com.ua.client_accounting.price.dto.create;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateServicePriceRequest {
    private String serviceName;
    private BigDecimal price;
}
