package com.ecommerce.customer.service;

import com.ecommerce.customer.config.ServiceConfig;
import com.ecommerce.customer.model.Customer;
import com.ecommerce.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
//    @Autowired
//    MessageSource messageSource;

    @Autowired
    private CustomerRepository customerRepository;

//    @Autowired
//    ServiceConfig serviceConfig;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    public Customer getCustomerById(long id) {
        return customerRepository.getReferenceById(id);
    }
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    public void deleteCustomer(long id) {
        customerRepository.deleteById(id);
    }
}