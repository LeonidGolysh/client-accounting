package com.ua.client_accounting.price.dto.update;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateServicePriceRequest {
    private String serviceName;
    private BigDecimal price;
}
