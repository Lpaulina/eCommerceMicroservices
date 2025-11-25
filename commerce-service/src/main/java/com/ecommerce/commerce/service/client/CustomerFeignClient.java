package com.ecommerce.commerce.service.client;

import com.ecommerce.commerce.model.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("customer-service")
public interface CustomerFeignClient {
    @GetMapping("v1/customers/{customerId}")
    CustomerResponse getCustomer(@PathVariable("customerId") Long customerId);
}