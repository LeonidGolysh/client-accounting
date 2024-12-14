package com.ua.client_accounting.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ua.client_accounting.price.entity.ServicePrice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_service")
public class OrderServicePriceEntity {

    @EmbeddedId
    private OrderServicePriceId id = new OrderServicePriceId();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @MapsId("idOrder")
    @JoinColumn(name = "id_order")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idService")
    @JoinColumn(name = "id_service")
    private ServicePrice servicePrice;
}
