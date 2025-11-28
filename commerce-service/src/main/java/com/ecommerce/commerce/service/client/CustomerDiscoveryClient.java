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

    @Autowired
    private RestTemplate restTemplate; // use the tracing-aware bean

    public CustomerResponse getCustomer(Long customerId) {
        List<ServiceInstance> instances = discoveryClient.getInstances("customer-service");

        if (instances.isEmpty()) return null;
        String serviceUri = String.format("%s/v1/customers/%s", instances.get(0).getUri(), customerId);

        ResponseEntity<CustomerResponse> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null,  // headers automatically propagated
                        CustomerResponse.class);

        return restExchange.getBody();
    }
}