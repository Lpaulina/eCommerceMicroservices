package com.ecommerce.customer.controller;

import com.ecommerce.customer.model.Notification;
import com.ecommerce.customer.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/notification")
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @GetMapping(value="/{notificationId}")
    public ResponseEntity<Notification> getNotification(@PathVariable("notificationId") Long notificationId){
        Notification notification = notificationService.findById(notificationId);
        return ResponseEntity.ok(notification);
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification){
        return ResponseEntity.ok(notificationService.save(notification));
    }
}