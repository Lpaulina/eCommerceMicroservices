package com.ecommerce.customer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "customer-client")
@Component
@Data
public class CustomerProperties {
    private String clientName;
}