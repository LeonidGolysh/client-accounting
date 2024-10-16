package com.ua.client_accounting.order.entity;

import com.ua.client_accounting.car.entity.Car;
import com.ua.client_accounting.client.entity.Client;
import com.ua.client_accounting.price.entity.ServicePrice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_car")
    private Car car;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @ManyToMany
    @JoinTable(
            name = "order_service",
            joinColumns = @JoinColumn(name = "id_order"),
            inverseJoinColumns = @JoinColumn(name = "id_service")
    )
    private Set<ServicePrice> servicePriceSet;
}
