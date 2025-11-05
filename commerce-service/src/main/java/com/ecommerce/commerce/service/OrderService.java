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

//    @CircuitBreaker(name = "orderService", fallbackMethod = "customFallbackOrderService")
    @RateLimiter(name = "orderService", fallbackMethod = "customFallbackOrderService")
    @Retry(name = "retryOrderService", fallbackMethod = "customFallbackOrderService")
    @Bulkhead(name = "bulkheadOrderService", type= Bulkhead.Type.SEMAPHORE, fallbackMethod = "customFallbackOrderService")
    public List<Order> getAllOrdersByCustomerId(Long customerId) {
        return orderRepository.findAllByCustomerId(customerId);
    }

//    @CircuitBreaker(name = "orderService", fallbackMethod = "customFallbackOrderService")
    @RateLimiter(name = "orderService", fallbackMethod = "customFallbackOrderService")
    @Retry(name = "retryOrderService", fallbackMethod = "customFallbackOrderService")
    @Bulkhead(name = "bulkheadOrderService", type= Bulkhead.Type.SEMAPHORE, fallbackMethod = "customFallbackOrderService")
    public Order findById(long id) {
        return orderRepository.findById(id).orElse(null);
    }

//    @CircuitBreaker(name = "orderService", fallbackMethod = "customFallbackOrderServiceDelete")
    @RateLimiter(name = "orderService", fallbackMethod = "customFallbackOrderServiceDelete")
    @Retry(name = "retryOrderService", fallbackMethod = "customFallbackOrderServiceDelete")
    @Bulkhead(name = "bulkheadOrderService", type= Bulkhead.Type.SEMAPHORE, fallbackMethod = "customFallbackOrderServiceDelete")
    public void deleteOrderById(long id) {
        orderRepository.deleteById(id);
    }

//    @CircuitBreaker(name = "orderService", fallbackMethod = "customFallbackOrderService")
    @RateLimiter(name = "orderService", fallbackMethod = "customFallbackOrderService")
    @Retry(name = "retryOrderService", fallbackMethod = "customFallbackOrderService")
    @Bulkhead(name = "bulkheadOrderService", type= Bulkhead.Type.SEMAPHORE, fallbackMethod = "customFallbackOrderService")
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
//    @CircuitBreaker(name = "orderService", fallbackMethod = "customFallbackOrderService")
    @RateLimiter(name = "orderService", fallbackMethod = "customFallbackOrderService")
    @Retry(name = "retryOrderService", fallbackMethod = "customFallbackOrderService")
    @Bulkhead(name = "bulkheadOrderService", type= Bulkhead.Type.SEMAPHORE, fallbackMethod = "customFallbackOrderService")
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    @SuppressWarnings("unused")
    private List<Order> customFallbackOrderService(Long customerId, Throwable t) {
        logger.warn("Fallback triggered for getAllOrdersByCustomerId({}): {}", customerId, t.toString());
        return List.of();
    }

    @SuppressWarnings("unused")
    private Order customFallbackOrderService(long id, Throwable t) {
        logger.warn("Fallback triggered for findById({}): {}", id, t.toString());
        Order fallbackOrder = new Order();
        fallbackOrder.setId(id);
        fallbackOrder.setTotalAmount(0);
        fallbackOrder.setTotalPrice(0.0);
        return fallbackOrder;
    }

    @SuppressWarnings("unused")
    private void customFallbackOrderServiceDelete(long id, Throwable t) {
        logger.warn("Fallback triggered for deleteOrderById({}): {}", id, t.toString());
    }

    @SuppressWarnings("unused")
    private Order customFallbackOrderService(Order order, Throwable t) {
        logger.warn("Fallback triggered for createOrder/updateOrder(): {}", t.toString());
        order.setTotalAmount(0);
        order.setTotalPrice(0.0);
        return order;
    }

}