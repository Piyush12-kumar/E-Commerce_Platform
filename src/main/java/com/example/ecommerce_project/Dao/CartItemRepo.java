package com.example.ecommerce_project.Dao;

import com.example.ecommerce_project.model.Cart;
import com.example.ecommerce_project.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Long> {
    void deleteByCart(Cart cart);
}
