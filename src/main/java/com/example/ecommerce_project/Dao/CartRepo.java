package com.example.ecommerce_project.Dao;

import com.example.ecommerce_project.model.Cart;
import com.example.ecommerce_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {

    Cart findByUser(User user);
}
