package com.ua.client_accounting.table.dto;

import com.ua.client_accounting.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MainTableDTO {
    private UUID orderId;
    private String clientName;
    private String phoneNumber;
    private String carModel;
    private String carColor;
    private String carNumberPlate;
    private Object services;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;

    public static MainTableDTO fromDTO(Order order) {
        return new MainTableDTO(
                order.getId(),
                order.getCar().getClient().getName(),
                order.getCar().getClient().getPhoneNumber(),
                order.getCar().getCarModel(),
                order.getCar().getCarColor(),
                order.getCar().getCarNumberPlate(),
                order.getOrderServicePriceEntityList().stream()
                        .map(osp -> osp.getServicePrice().getId())
                        .collect(Collectors.toList()),
                order.getOrderDate(),
                order.getOrderServicePriceEntityList().stream()
                        .map(osp -> osp.getServicePrice().getPrice())
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }
}