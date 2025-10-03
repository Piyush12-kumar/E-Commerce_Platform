package com.example.ecommerce_project.Dao;

import com.example.ecommerce_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    boolean existsUsersByUsername(String username);

    boolean existsUsersByEmail(String email);
}