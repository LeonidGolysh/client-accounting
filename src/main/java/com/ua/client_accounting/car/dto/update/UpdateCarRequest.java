package com.ua.client_accounting.car.dto.update;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateCarRequest {
    private UUID clientId;
    private String carModel;
    private String carColor;
    private String carNumberPlate;
}
