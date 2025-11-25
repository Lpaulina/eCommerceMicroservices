package com.ecommerce.customer.model;

import java.util.List;

public class CustomerListWrapper {
    private List<Customer> customers;

    public CustomerListWrapper() {}

    public CustomerListWrapper(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}