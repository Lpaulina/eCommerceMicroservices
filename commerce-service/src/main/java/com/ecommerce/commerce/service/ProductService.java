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

    @RateLimiter(name = "productService", fallbackMethod = "customFallbackProductService")
	@Retry(name = "retryProductService", fallbackMethod = "customFallbackProductService")
    @Bulkhead(name = "bulkheadProductService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackProductService")
    @CircuitBreaker(name = "productService", fallbackMethod = "customFallbackProductService")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @RateLimiter(name = "productService", fallbackMethod = "customFallbackProductService")
    @Retry(name = "retryProductService", fallbackMethod = "customFallbackProductService")
    @Bulkhead(name = "bulkheadProductService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackProductService")
    @CircuitBreaker(name = "productService", fallbackMethod = "customFallbackProductService")
    public Product getProduct(long id) {
        return productRepository.findById(id).get();
    }

    @RateLimiter(name = "productService", fallbackMethod = "customFallbackProductService")
    @Retry(name = "retryProductService", fallbackMethod = "customFallbackProductService")
    @Bulkhead(name = "bulkheadProductService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackProductService")
    @CircuitBreaker(name = "productService", fallbackMethod = "customFallbackProductService")
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @RateLimiter(name = "productService", fallbackMethod = "customFallbackProductService")
    @Retry(name = "retryProductService", fallbackMethod = "customFallbackProductService")
    @Bulkhead(name = "bulkheadProductService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackProductService")
    @CircuitBreaker(name = "productService", fallbackMethod = "customFallbackProductService")
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @RateLimiter(name = "productService", fallbackMethod = "customFallbackProductService")
    @Retry(name = "retryProductService", fallbackMethod = "customFallbackProductService")
    @Bulkhead(name = "bulkheadProductService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackProductService")
    @CircuitBreaker(name = "productService", fallbackMethod = "customFallbackProductService")
    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }

    @SuppressWarnings("unused")
    private String customFallbackProductService(Throwable t) {
        logger.debug("Fallback triggered by: {}", t.getClass().getSimpleName());
        return "Unable to execute action for Product";
    }

}