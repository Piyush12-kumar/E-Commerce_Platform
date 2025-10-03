package com.example.ecommerce_project.Dao;

import com.example.ecommerce_project.model.Order;
import com.example.ecommerce_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
