package com.ua.client_accounting.order.repository;

import com.ua.client_accounting.car.entity.Car;
import com.ua.client_accounting.order.entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByCar(Car car);
}
