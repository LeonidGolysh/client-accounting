package com.ua.client_accounting.car.dto.create;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateCarRequest {
    private UUID clientId;
    private String carModel;
    private String carColor;
    private String carNumberPlate;
}
