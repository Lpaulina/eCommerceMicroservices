package com.ecommerce.commerce.repository;

import com.ecommerce.commerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findAllByCustomerId(Long customerId);
}