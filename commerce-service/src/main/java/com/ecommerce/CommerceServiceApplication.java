package com.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@RefreshScope
public class CommerceServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommerceServiceApplication.class, args);
    }
}