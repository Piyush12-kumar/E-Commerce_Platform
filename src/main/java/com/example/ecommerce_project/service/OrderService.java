// ecommerce_project/service/OrderService.java
package com.example.ecommerce_project.service;

import com.example.ecommerce_project.Dao.OrderItemRepo;
import com.example.ecommerce_project.Dao.OrderRepo;
import com.example.ecommerce_project.Dao.ProductRepo;
import com.example.ecommerce_project.exception.ResourceNotFoundException;
import com.example.ecommerce_project.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private ProductRepo productRepo;

    @Transactional
    public Order createOrder() {
        User user = userService.getCurrentUser();
        Cart cart = cartService.getCartItems();

        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty, cannot create order");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            // Validate and reduce stock
            if (product.getStock() == null || product.getStock() < cartItem.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepo.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPricePerUnit(product.getPrice());

            BigDecimal itemTotalPrice = orderItem.getPricePerUnit().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            orderItem.setTotalPrice(itemTotalPrice);

            orderItem.setOrder(order);
            orderItems.add(orderItem);

            totalAmount = totalAmount.add(itemTotalPrice);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepo.save(order);
        orderItemRepo.saveAll(orderItems);

        cartService.clearCart();

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        User user = userService.getCurrentUser();
        if (user.getOrders() == null || user.getOrders().isEmpty()) {
            return new ArrayList<>();
        }
        return user.getOrders().stream().toList();
    }

    public Order getOrderById(Long orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    public Order cancelOrderById(Long orderId) {
        User user = userService.getCurrentUser();
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new ResourceNotFoundException("Order does not belong to the current user");
        }

        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled");
        }

        if (order.getStatus() == Order.OrderStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed order");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        return orderRepo.save(order);
    }
}