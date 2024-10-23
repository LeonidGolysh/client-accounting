package com.ua.client_accounting.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderDTO {
    private UUID id;
    private UUID carId;
    private LocalDateTime orderDate;
    private List<OrderServiceDTO> serviceDTOList;
}
