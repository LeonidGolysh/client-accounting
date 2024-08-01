package com.ua.client_accounting.car.entity;

import com.ua.client_accounting.client.entity.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;

    @Column(name = "car_model")
    private String carModel;

    @Column(name = "car_color")
    private String carColor;

    @Column(name = "car_number_plate")
    private String carNumberPlate;
}
