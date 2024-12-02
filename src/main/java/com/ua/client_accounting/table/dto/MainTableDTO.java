package com.ua.client_accounting.table.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainTableDTO {
    private UUID carId;
    private UUID orderId;
    private String clientName;
    private String phoneNumber;
    private String carModel;
    private String carColor;
    private String carNumberPlate;
    private Object services;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;
}