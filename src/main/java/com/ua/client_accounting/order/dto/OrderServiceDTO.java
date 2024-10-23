package com.ua.client_accounting.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderServiceDTO {
    private String serviceName;
    private BigDecimal price;
}
