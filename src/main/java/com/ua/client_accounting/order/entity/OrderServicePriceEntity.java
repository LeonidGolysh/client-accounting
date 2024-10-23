package com.ua.client_accounting.order.entity;

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

    @ManyToOne
    @MapsId("idOrder")
    @JoinColumn(name = "id_order")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    @ManyToOne
    @MapsId("idService")
    @JoinColumn(name = "id_service")
    private ServicePrice servicePrice;
}
