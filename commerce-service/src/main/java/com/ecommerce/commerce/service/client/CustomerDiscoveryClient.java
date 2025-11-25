package com.ecommerce.commerce.service.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.commerce.model.CustomerResponse;

@Component
public class CustomerDiscoveryClient {

    @Autowired
    private DiscoveryClient discoveryClient;

    public CustomerResponse getCustomer(Long customerId) {
        RestTemplate restTemplate = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("customer-service");

        if (instances.isEmpty()) return null;
        String serviceUri = String.format("%s/v1/customers/%s",instances.getFirst().getUri().toString(), customerId);

        ResponseEntity< CustomerResponse > restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, CustomerResponse.class, customerId);

        return restExchange.getBody();
    }
}