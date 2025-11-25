package com.ecommerce.customer.service;

import com.ecommerce.customer.model.Customer;
import com.ecommerce.customer.model.CustomerListWrapper;
import com.ecommerce.customer.repository.CustomerRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    // Full list cache
    @RateLimiter(name = "customerService", fallbackMethod = "getAllFallbackCustomerService")
    @Retry(name = "retryCustomerService", fallbackMethod = "getAllFallbackCustomerService")
    @Bulkhead(name = "bulkheadCustomerService", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "getAllFallbackCustomerService")
    @Cacheable(value = "customersAll", key = "'all'")
    public List<Customer> getAllCustomers() {
        logger.info("DB HIT: fetching customers from database");
        return customerRepository.findAll();
    }

    // Single customer cache
    @RateLimiter(name = "customerService", fallbackMethod = "getFallbackCustomerService")
    @Retry(name = "retryCustomerService", fallbackMethod = "getFallbackCustomerService")
    @Bulkhead(name = "bulkheadCustomerService", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "getFallbackCustomerService")
    @Cacheable(value = "customersById", key = "#id")
    public Customer getCustomerById(long id) {
        return customerRepository.findById(id).orElse(null);
    }

    // Create customer: update individual cache, evict full list cache
    @RateLimiter(name = "customerService", fallbackMethod = "createFallbackCustomerService")
    @Retry(name = "retryCustomerService", fallbackMethod = "createFallbackCustomerService")
    @Bulkhead(name = "bulkheadCustomerService", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "createFallbackCustomerService")
    @Caching(
            put = @CachePut(value = "customersById", key = "#customer.id"),
            evict = @CacheEvict(value = "customersAll", key = "'all'")
    )
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // Update customer: update individual cache, evict full list cache
    @RateLimiter(name = "customerService", fallbackMethod = "updateFallbackCustomerService")
    @Retry(name = "retryCustomerService", fallbackMethod = "updateFallbackCustomerService")
    @Bulkhead(name = "bulkheadCustomerService", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "updateFallbackCustomerService")
    @Caching(
            put = @CachePut(value = "customersById", key = "#customer.id"),
            evict = @CacheEvict(value = "customersAll", key = "'all'")
    )
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // Delete customer: evict both individual and full list caches
    @RateLimiter(name = "customerService", fallbackMethod = "deleteFallbackCustomerService")
    @Retry(name = "retryCustomerService", fallbackMethod = "deleteFallbackCustomerService")
    @Bulkhead(name = "bulkheadCustomerService", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "deleteFallbackCustomerService")
    @Caching(evict = {
            @CacheEvict(value = "customersById", key = "#id"),
            @CacheEvict(value = "customersAll", key = "'all'")
    })
    public void deleteCustomer(long id) {
        customerRepository.deleteById(id);
    }

    // Fallbacks
    public List<Customer> getAllFallbackCustomerService(Throwable t) {
        logger.warn("Fallback triggered for getAllCustomers(): {}", t.toString());
        return Collections.emptyList();
    }

    public Customer getFallbackCustomerService(long id, Throwable t) {
        logger.warn("Fallback triggered for getCustomerById({}): {}", id, t.toString());
        Customer fallbackCustomer = new Customer();
        fallbackCustomer.setId(id);
        fallbackCustomer.setName("Unavailable");
        fallbackCustomer.setEmail("unavailable@example.com");
        fallbackCustomer.setPhoneNumber("N/A");
        fallbackCustomer.setStreet("N/A");
        fallbackCustomer.setCity("N/A");
        fallbackCustomer.setState("N/A");
        fallbackCustomer.setZip("00000");
        fallbackCustomer.setCountry("N/A");
        return fallbackCustomer;
    }

    public Customer createFallbackCustomerService(Customer customer, Throwable t) {
        logger.warn("Fallback triggered for createCustomer(): {}", t.toString());
        customer.setId(-1L);
        return customer;
    }

    public Customer updateFallbackCustomerService(Customer customer, Throwable t) {
        logger.warn("Fallback triggered for updateCustomer(): {}", t.toString());
        customer.setName(customer.getName() + " (update failed)");
        return customer;
    }

    public void deleteFallbackCustomerService(long id, Throwable t) {
        logger.warn("Fallback triggered for deleteCustomer({}): {}", id, t.toString());
    }

}