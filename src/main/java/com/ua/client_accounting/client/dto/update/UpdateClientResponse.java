package com.ua.client_accounting.client.dto.update;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateClientResponse {
    private UUID clientId;
    private String name;
    private String phoneNumber;
}
