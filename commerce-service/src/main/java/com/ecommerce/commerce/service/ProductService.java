package com.ecommerce.commerce.service;

import com.ecommerce.commerce.model.Product;
import com.ecommerce.commerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
    public Product getProduct(long id) {
        return productRepository.findById(id).get();
    }
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }
    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }

}