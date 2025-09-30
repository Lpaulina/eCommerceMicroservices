package com.ecommerce.customer.controller;

import com.ecommerce.customer.model.Customer;
import com.ecommerce.customer.repository.CustomerRepository;
import com.ecommerce.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers()
    {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok().body(customers);
    }

    @RequestMapping(value="/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<Customer> getCustomer(@PathVariable("customerId") Long customerId)
    {
        Customer customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok().body(customer);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer)
    {
        Customer createdCustomer = customerService.createCustomer(customer);
        return ResponseEntity.ok().body(createdCustomer);
    }

    @PutMapping
    public  ResponseEntity<Customer> updateCustomer(@RequestBody Customer updatedCustomer)
    {
        Customer customer = customerService.updateCustomer(updatedCustomer);
        return ResponseEntity.ok().body(customer);
    }
    @DeleteMapping(value="/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("customerId") Long customerId)
    {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok("Customer deleted");
    }
}