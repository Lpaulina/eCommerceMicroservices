package com.ecommerce.customer.controller;

import com.ecommerce.customer.model.Customer;
import com.ecommerce.customer.service.CustomerService;
import com.ecommerce.customer.service.KafkaProducerService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/customers")
public class CustomerController {
    private final KafkaProducerService kafkaProducerService;



    @Autowired
    private CustomerService customerService;

    public CustomerController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    //    @RolesAllowed({ "customer-admin" })
    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers()
    {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok().body(customers);
    }

//    @RolesAllowed({ "customer-admin" })
    @RequestMapping(value="/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<Customer> getCustomer(@PathVariable("customerId") Long customerId)
    {
        Customer customer = customerService.getCustomerById(customerId);

        if (customer == null){
            kafkaProducerService.sendMessage("Customer with id " + customerId + " not found");
            return ResponseEntity.notFound().build();
        }

        kafkaProducerService.sendMessage("Customer with id " + customerId + " found");
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

//    @RolesAllowed({ "customer-admin" })
    @DeleteMapping(value="/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("customerId") Long customerId)
    {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok("Customer deleted");
    }
}