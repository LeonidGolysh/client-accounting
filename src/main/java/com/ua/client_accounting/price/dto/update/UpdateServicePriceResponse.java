package com.ua.client_accounting.price.dto.update;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateServicePriceResponse {
    private Long id;
    private String serviceName;
    private BigDecimal price;
}
