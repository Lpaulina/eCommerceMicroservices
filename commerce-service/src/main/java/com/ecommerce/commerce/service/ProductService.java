package com.ecommerce.commerce.service;

import com.ecommerce.commerce.config.ServiceConfig;
import com.ecommerce.commerce.model.Product;
import com.ecommerce.commerce.repository.ProductRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;


import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    ServiceConfig config;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

//    @CircuitBreaker(name = "productService", fallbackMethod = "customFallbackProductService")
    @RateLimiter(name = "productService", fallbackMethod = "customFallbackProductService")
	@Retry(name = "retryProductService", fallbackMethod = "customFallbackProductService")
    @Bulkhead(name = "bulkheadProductService", type= Bulkhead.Type.SEMAPHORE, fallbackMethod = "customFallbackProductService")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

//    @CircuitBreaker(name = "productService", fallbackMethod = "customFallbackProductService")
    @RateLimiter(name = "productService", fallbackMethod = "customFallbackProductService")
    @Retry(name = "retryProductService", fallbackMethod = "customFallbackProductService")
    @Bulkhead(name = "bulkheadProductService", type= Bulkhead.Type.SEMAPHORE, fallbackMethod = "customFallbackProductService")
    public Product getProduct(long id) {
        return productRepository.findById(id).get();
    }

//    @CircuitBreaker(name = "productService", fallbackMethod = "customFallbackProductService")
    @RateLimiter(name = "productService", fallbackMethod = "customFallbackProductService")
    @Retry(name = "retryProductService", fallbackMethod = "customFallbackProductService")
    @Bulkhead(name = "bulkheadProductService", type= Bulkhead.Type.SEMAPHORE, fallbackMethod = "customFallbackProductService")
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

//    @CircuitBreaker(name = "productService", fallbackMethod = "customFallbackProductService")
    @RateLimiter(name = "productService", fallbackMethod = "customFallbackProductService")
    @Retry(name = "retryProductService", fallbackMethod = "customFallbackProductService")
    @Bulkhead(name = "bulkheadProductService", type= Bulkhead.Type.SEMAPHORE, fallbackMethod = "customFallbackProductService")
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

//    @CircuitBreaker(name = "productService", fallbackMethod = "customFallbackProductService")
    @RateLimiter(name = "productService", fallbackMethod = "customFallbackProductService")
    @Retry(name = "retryProductService", fallbackMethod = "customFallbackProductService")
    @Bulkhead(name = "bulkheadProductService", type= Bulkhead.Type.SEMAPHORE, fallbackMethod = "customFallbackProductService")
    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }

    @SuppressWarnings("unused")
    private List<Product> customFallbackProductService(Throwable t) {
        logger.warn("Fallback triggered for getProducts(): {}", t.toString());
        return List.of(new Product());
    }

    @SuppressWarnings("unused")
    private Product customFallbackProductService(long id, Throwable t) {
        logger.warn("Fallback triggered for getProduct({}): {}", id, t.toString());
        return new Product();
    }

    @SuppressWarnings("unused")
    private Product customFallbackProductService(Product product, Throwable t) {
        logger.error("Fallback triggered for add/updateProduct() due to:", t);
        return product;
    }

}