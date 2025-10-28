package com.ecommerce.customer.service;

import com.ecommerce.customer.model.Notification;
import com.ecommerce.customer.repository.NotificationRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @RateLimiter(name = "notificationService", fallbackMethod = "customFallbackNotificationService")
    @Retry(name = "retryNotificationService", fallbackMethod = "customFallbackNotificationService")
    @Bulkhead(name = "bulkheadNotificationService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackNotificationService")
    @CircuitBreaker(name = "notificationService", fallbackMethod = "customFallbackNotificationService")
    public Notification save(Notification notification) {
        notification.setSentDate(new Date());
        return notificationRepository.save(notification);
    }

    @RateLimiter(name = "notificationService", fallbackMethod = "customFallbackNotificationService")
    @Retry(name = "retryNotificationService", fallbackMethod = "customFallbackNotificationService")
    @Bulkhead(name = "bulkheadNotificationService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackNotificationService")
    @CircuitBreaker(name = "notificationService", fallbackMethod = "customFallbackNotificationService")
    public Notification findById(long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @SuppressWarnings("unused")
    private String customFallbackNotificationService(Throwable t) {
        logger.debug("Fallback triggered by: {}", t.getClass().getSimpleName());
        return "Unable to execute action for Product";
    }
}