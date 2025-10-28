package com.ecommerce.commerce.service;

import com.ecommerce.commerce.model.Order;
import com.ecommerce.commerce.model.OrderItem;
import com.ecommerce.commerce.model.Product;
import com.ecommerce.commerce.repository.OrderRepository;
import com.ecommerce.commerce.repository.ProductRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @RateLimiter(name = "orderService", fallbackMethod = "customFallbackOrderService")
    @Retry(name = "retryOrderService", fallbackMethod = "customFallbackOrderService")
    @Bulkhead(name = "bulkheadOrderService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackOrderService")
    @CircuitBreaker(name = "orderService", fallbackMethod = "customFallbackOrderService")
    public List<Order> getAllOrdersByCustomerId(Long customerId) {
        return orderRepository.findAllByCustomerId(customerId);
    }

    @RateLimiter(name = "orderService", fallbackMethod = "customFallbackOrderService")
    @Retry(name = "retryOrderService", fallbackMethod = "customFallbackOrderService")
    @Bulkhead(name = "bulkheadOrderService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackOrderService")
    @CircuitBreaker(name = "orderService", fallbackMethod = "customFallbackOrderService")
    public Order findById(long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @RateLimiter(name = "orderService", fallbackMethod = "customFallbackOrderService")
    @Retry(name = "retryOrderService", fallbackMethod = "customFallbackOrderService")
    @Bulkhead(name = "bulkheadOrderService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackOrderService")
    @CircuitBreaker(name = "orderService", fallbackMethod = "customFallbackOrderService")
    public void deleteOrderById(long id) {
        orderRepository.deleteById(id);
    }

    @RateLimiter(name = "orderService", fallbackMethod = "customFallbackOrderService")
    @Retry(name = "retryOrderService", fallbackMethod = "customFallbackOrderService")
    @Bulkhead(name = "bulkheadOrderService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackOrderService")
    @CircuitBreaker(name = "orderService", fallbackMethod = "customFallbackOrderService")
    public Order createOrder(Order order) {
        double totalPrice = 0;
        int totalAmount = 0;

        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                item.setOrder(order);

                Product product = productRepository.findById(item.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProduct().getId()));
                product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
                productRepository.save(product);

                item.setProduct(product);

                totalAmount += item.getQuantity();
                totalPrice += item.getQuantity() * product.getPrice();
            }
        }

        order.setTotalAmount(totalAmount);
        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }

    @RateLimiter(name = "orderService", fallbackMethod = "customFallbackOrderService")
    @Retry(name = "retryOrderService", fallbackMethod = "customFallbackOrderService")
    @Bulkhead(name = "bulkheadOrderService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "customFallbackOrderService")
    @CircuitBreaker(name = "orderService", fallbackMethod = "customFallbackOrderService")
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    @SuppressWarnings("unused")
    private String customFallbackOrderService(Throwable t) {
        logger.debug("Fallback triggered by: {}", t.getClass().getSimpleName());
        return "Unable to execute action for Order";
    }
}