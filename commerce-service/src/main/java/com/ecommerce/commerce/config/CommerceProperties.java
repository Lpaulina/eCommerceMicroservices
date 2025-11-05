package com.ecommerce.commerce.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "commerce-client")
@Component
@Data
public class CommerceProperties {
    private String clientName;
}