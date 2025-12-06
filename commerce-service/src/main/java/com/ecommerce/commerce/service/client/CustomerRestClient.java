package com.ecommerce.commerce.service.client;

import com.ecommerce.commerce.model.CustomerResponse;
import com.ecommerce.commerce.utils.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerRestClient {

    private final RestTemplate restTemplate;
    private final TokenProvider tokenProvider;

    public CustomerRestClient(RestTemplateBuilder restTemplateBuilder, TokenProvider tokenProvider) {
        this.restTemplate = restTemplateBuilder.build();
        this.tokenProvider = tokenProvider;
    }

    public CustomerResponse getCustomer(Long id) {
        String token = tokenProvider.getCurrentToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<CustomerResponse> response = restTemplate.exchange(
                "http://customer-service:8082/v1/customers/" + id,
                HttpMethod.GET,
                entity,
                CustomerResponse.class
        );

        return response.getBody();
    }
}