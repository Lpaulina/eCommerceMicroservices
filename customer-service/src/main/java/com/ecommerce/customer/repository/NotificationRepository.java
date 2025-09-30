package com.ecommerce.customer.repository;

import com.ecommerce.customer.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}