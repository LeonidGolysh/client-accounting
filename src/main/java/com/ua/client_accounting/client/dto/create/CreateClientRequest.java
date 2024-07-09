package com.ua.client_accounting.client.dto.create;

import lombok.Data;

@Data
public class CreateClientRequest {
    private String name;
    private String phoneNumber;
}
