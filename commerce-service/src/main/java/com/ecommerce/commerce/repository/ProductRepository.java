package com.ecommerce.commerce.repository;

import com.ecommerce.commerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}