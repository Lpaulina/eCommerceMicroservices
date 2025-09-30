package com.ecommerce.commerce.service;

import com.ecommerce.commerce.model.Order;
import com.ecommerce.commerce.model.OrderItem;
import com.ecommerce.commerce.model.Product;
import com.ecommerce.commerce.repository.OrderRepository;
import com.ecommerce.commerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<Order> getAllOrdersByCustomerId(Long customerId) {
        return orderRepository.findAllByCustomerId(customerId);
    }

    public Order findById(long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void deleteOrderById(long id) {
        orderRepository.deleteById(id);
    }

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

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }
}