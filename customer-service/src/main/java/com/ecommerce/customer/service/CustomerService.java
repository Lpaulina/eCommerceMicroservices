package com.ecommerce.customer.service;

import com.ecommerce.customer.config.ServiceConfig;
import com.ecommerce.customer.model.Customer;
import com.ecommerce.customer.repository.CustomerRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    ServiceConfig serviceConfig;

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @RateLimiter(name = "customerService", fallbackMethod = "customFallbackCustomerService")
    @Retry(name = "retryCustomerService", fallbackMethod = "customFallbackCustomerService")
    @Bulkhead(name = "bulkheadCustomerService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackCustomerService")
    @CircuitBreaker(name = "customerService", fallbackMethod = "customFallbackCustomerService")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @RateLimiter(name = "customerService", fallbackMethod = "customFallbackCustomerService")
    @Retry(name = "retryCustomerService", fallbackMethod = "customFallbackCustomerService")
    @Bulkhead(name = "bulkheadCustomerService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackCustomerService")
    @CircuitBreaker(name = "customerService", fallbackMethod = "customFallbackCustomerService")
    public Customer getCustomerById(long id) {
        return customerRepository.getReferenceById(id);
    }

    @RateLimiter(name = "customerService", fallbackMethod = "customFallbackCustomerService")
    @Retry(name = "retryCustomerService", fallbackMethod = "customFallbackCustomerService")
    @Bulkhead(name = "bulkheadCustomerService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackCustomerService")
    @CircuitBreaker(name = "customerService", fallbackMethod = "customFallbackCustomerService")
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @RateLimiter(name = "customerService", fallbackMethod = "customFallbackCustomerService")
    @Retry(name = "retryCustomerService", fallbackMethod = "customFallbackCustomerService")
    @Bulkhead(name = "bulkheadCustomerService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackCustomerService")
    @CircuitBreaker(name = "customerService", fallbackMethod = "customFallbackCustomerService")
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @RateLimiter(name = "customerService", fallbackMethod = "customFallbackCustomerService")
    @Retry(name = "retryCustomerService", fallbackMethod = "customFallbackCustomerService")
    @Bulkhead(name = "bulkheadCustomerService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackCustomerService")
    @CircuitBreaker(name = "customerService", fallbackMethod = "customFallbackCustomerService")
    public void deleteCustomer(long id) {
        customerRepository.deleteById(id);
    }

    @SuppressWarnings("unused")
    private String customFallbackCustomerService(Throwable t) {
        logger.debug("Fallback triggered by: {}", t.getClass().getSimpleName());
        return "Unable to execute action for Product";
    }
}