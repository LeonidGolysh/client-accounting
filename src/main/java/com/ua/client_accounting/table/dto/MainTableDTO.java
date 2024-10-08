package com.ua.client_accounting.table.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainTableDTO {
    private UUID carId;
    private String clientName;
    private String phoneNumber;
    private String carModel;
    private String carColor;
    private String carNumberPlate;
}
