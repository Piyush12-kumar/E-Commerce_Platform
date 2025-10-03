package com.example.ecommerce_project.Dao;

import com.example.ecommerce_project.model.Product;
import com.example.ecommerce_project.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {

    List<Review> findByProduct(Product product);
}
