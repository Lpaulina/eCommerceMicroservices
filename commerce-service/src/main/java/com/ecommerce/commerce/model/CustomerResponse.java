package com.ecommerce.commerce.model;

public record CustomerResponse(
        long id,

        String name,
        String email,
        String phoneNumber,
        String password,
        String street,
        String city,
        String state,
        String zip,
        String country
) {}