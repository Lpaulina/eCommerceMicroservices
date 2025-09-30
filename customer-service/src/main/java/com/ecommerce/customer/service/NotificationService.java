package com.ecommerce.customer.service;

import com.ecommerce.customer.model.Notification;
import com.ecommerce.customer.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public Notification save(Notification notification) {
        notification.setSentDate(new Date());
        return notificationRepository.save(notification);
    }

    public Notification findById(long id) {
        return notificationRepository.findById(id).orElse(null);
    }
}