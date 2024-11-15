package com.ua.client_accounting.order.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ua.client_accounting.price.entity.ServicePrice;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@Table(name = "order_service")
public class OrderServicePriceEntity {

    @EmbeddedId
    private OrderServicePriceId id = new OrderServicePriceId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idOrder")
    @JoinColumn(name = "id_order")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idService")
    @JoinColumn(name = "id_service")
    private ServicePrice servicePrice;
}
