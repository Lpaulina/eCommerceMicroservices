//package com.ecommerce.commerce.service.client;
//
//import com.ecommerce.customer.model.Customer;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//@FeignClient("customer-service")
//public interface CustomerFeignClient {
//    @GetMapping("v1/customers/{customerId}")
//    Customer getCustomer(@PathVariable("customerId")  Long customerId);
//}