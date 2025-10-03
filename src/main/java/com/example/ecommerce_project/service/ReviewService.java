package com.example.ecommerce_project.service;

import com.example.ecommerce_project.DTO.ReviewDto;
import com.example.ecommerce_project.Dao.ProductRepo;
import com.example.ecommerce_project.Dao.ReviewRepo;
import com.example.ecommerce_project.exception.ResourceNotFoundException;
import com.example.ecommerce_project.model.Product;
import com.example.ecommerce_project.model.Review;
import com.example.ecommerce_project.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    ReviewRepo reviewRepo;
    @Autowired
    UserService userService;
    @Autowired
    ProductRepo productRepo;

    @Transactional
    public Review addReview(Review review) {
        User user = userService.getCurrentUser();
        Product product = productRepo.findById(review.getProduct().getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        review.setUser(user);
        review.setProduct(product);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (review.getComment() == null || review.getComment().isEmpty()) {
            throw new IllegalArgumentException("Comment cannot be null or empty");
        }
        return reviewRepo.save(review);
    }

    public Review updateReview(Long reviewId, ReviewDto reviewDto) {
        Review existingReview = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        // Add validation for the currently logged-in user to ensure they own the review
        User currentUser = userService.getCurrentUser();
        if (!existingReview.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new SecurityException("User not authorized to update this review");
        }

        existingReview.setRating(reviewDto.getRating());
        existingReview.setComment(reviewDto.getComment());
        existingReview.setUpdatedAt(LocalDateTime.now());
        return reviewRepo.save(existingReview);
    }

    public boolean deleteReview(Long reviewId) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        // Add validation for user ownership or admin role
        User currentUser = userService.getCurrentUser();
        if (!review.getUser().getUserId().equals(currentUser.getUserId()) && !currentUser.getRoles().contains("ADMIN")) {
            throw new SecurityException("User not authorized to delete this review");
        }

        reviewRepo.delete(review);
        return true;
    }

    public List<Review> getReviewsByProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return reviewRepo.findByProduct(product);
    }
}