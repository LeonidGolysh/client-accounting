package com.ua.client_accounting.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderServicePriceId implements Serializable {
    @Column(name = "id_order")
    private UUID idOrder;

    @Column(name = "id_service")
    private Long idService;
}
