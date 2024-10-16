package com.ua.client_accounting.order.dto.create;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class CreateOrderRequest {
    private UUID car;
    private LocalDateTime orderDate;
    private Set<Long> serviceIds;
}
