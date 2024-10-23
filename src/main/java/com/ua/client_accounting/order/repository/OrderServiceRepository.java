package com.ua.client_accounting.order.repository;

import com.ua.client_accounting.order.entity.Order;
import com.ua.client_accounting.order.entity.OrderServicePriceEntity;
import com.ua.client_accounting.order.entity.OrderServicePriceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderServiceRepository  extends JpaRepository<OrderServicePriceEntity, OrderServicePriceId> {

    // Delete all records by order
    void deleteByOrder(Order order);
}
