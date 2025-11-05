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

import java.util.Collections;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    ServiceConfig serviceConfig;

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    // @CircuitBreaker(name = "customerService", fallbackMethod = "getAllFallbackCustomerService")
    @RateLimiter(name = "customerService", fallbackMethod = "getAllFallbackCustomerService")
    @Retry(name = "retryCustomerService", fallbackMethod = "getAllFallbackCustomerService")
    @Bulkhead(name = "bulkheadCustomerService", type= Bulkhead.Type.SEMAPHORE, fallbackMethod = "getAllFallbackCustomerService")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

//  @CircuitBreaker(name = "customerService", fallbackMethod = "getFallbackCustomerService")
    @RateLimiter(name = "customerService", fallbackMethod = "getFallbackCustomerService")
    @Retry(name = "retryCustomerService", fallbackMethod = "getFallbackCustomerService")
    @Bulkhead(name = "bulkheadCustomerService", type= Bulkhead.Type.SEMAPHORE, fallbackMethod = "getFallbackCustomerService")
    public Customer getCustomerById(long id) {
        return customerRepository.getReferenceById(id);
    }
//    # @CircuitBreaker(name = "customerService", fallbackMethod = "createFallbackCustomerService")
    @RateLimiter(name = "customerService", fallbackMethod = "createFallbackCustomerService")
    @Retry(name = "retryCustomerService", fallbackMethod = "createFallbackCustomerService")
    @Bulkhead(name = "bulkheadCustomerService", type= Bulkhead.Type.SEMAPHORE, fallbackMethod = "createFallbackCustomerService")
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

//    @CircuitBreaker(name = "customerService", fallbackMethod = "updateFallbackCustomerService")
    @RateLimiter(name = "customerService", fallbackMethod = "updateFallbackCustomerService")
    @Retry(name = "retryCustomerService", fallbackMethod = "updateFallbackCustomerService")
    @Bulkhead(name = "bulkheadCustomerService", type= Bulkhead.Type.SEMAPHORE, fallbackMethod = "updateFallbackCustomerService")
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

//    @CircuitBreaker(name = "customerService", fallbackMethod = "deleteFallbackCustomerService")
    @RateLimiter(name = "customerService", fallbackMethod = "deleteFallbackCustomerService")
    @Retry(name = "retryCustomerService", fallbackMethod = "deleteFallbackCustomerService")
    @Bulkhead(name = "bulkheadCustomerService", type= Bulkhead.Type.SEMAPHORE, fallbackMethod = "deleteFallbackCustomerService")
    public void deleteCustomer(long id) {
        customerRepository.deleteById(id);
    }
    public List<Customer> getAllFallbackCustomerService(Throwable t) {
        logger.warn("Fallback triggered for getAllCustomers(): {}", t.toString());

        return Collections.emptyList();
    }

    public Customer getFallbackCustomerService(long id, Throwable t) {
        logger.warn("Fallback triggered for getCustomerById(" + id +"): {}", t.toString());

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

        Customer fallback = new Customer();
        fallback.setName(customer.getName());
        fallback.setEmail(customer.getEmail());
        fallback.setPhoneNumber(customer.getPhoneNumber());
        fallback.setStreet(customer.getStreet());
        fallback.setCity(customer.getCity());
        fallback.setState(customer.getState());
        fallback.setZip(customer.getZip());
        fallback.setCountry(customer.getCountry());
        fallback.setId(-1L); // indicate failure
        return fallback;
    }

    public Customer updateFallbackCustomerService(Customer customer, Throwable t) {
        logger.warn("Fallback triggered for updateCustomer(): {}", t.toString());
        Customer fallback = new Customer();
        fallback.setId(customer.getId());
        fallback.setName(customer.getName() + " (update failed)");
        fallback.setEmail(customer.getEmail());
        return fallback;
    }

    public void deleteFallbackCustomerService(long id, Throwable t) {
        logger.warn("Fallback triggered for deleteCustomer(" + id + "): {}", t.toString());
    }

}