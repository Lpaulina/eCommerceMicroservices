package com.ecommerce.commerce.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "customer", groupId = "commerce-service")
    public void consume(String message) {
        System.out.println("Message received: " + message);
    }
}