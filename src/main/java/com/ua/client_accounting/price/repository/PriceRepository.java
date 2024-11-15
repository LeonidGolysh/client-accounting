package com.ua.client_accounting.price.repository;

import com.ua.client_accounting.price.entity.ServicePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<ServicePrice, Long> {
    Optional<ServicePrice> findByServiceName(String serviceName);
}
