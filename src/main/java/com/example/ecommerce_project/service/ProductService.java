package com.example.ecommerce_project.service;

import com.example.ecommerce_project.Dao.ProductRepo;
import com.example.ecommerce_project.exception.ResourceNotFoundException;
import com.example.ecommerce_project.model.Product;
import com.example.ecommerce_project.model.Review;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepo productRepo;

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Transactional(readOnly = true)
    public Page<Product> findProducts(String name, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Boolean featured, Boolean active, Pageable pageable) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }
            if(categoryId!=null){
                predicates.add(criteriaBuilder.equal(root.get("category").get("categoryId"), categoryId));
            }

            if(minPrice != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if(maxPrice != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if(featured!=null){
                predicates.add(criteriaBuilder.equal(root.get("featured"), featured));
            }

            if(active!=null){
                predicates.add(criteriaBuilder.equal(root.get("active"), active));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Product> products = productRepo.findAll(spec, pageable);
        // Build full image URLs without modifying managed entities
        products.forEach(this::buildImageUrl);
        return products;
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        Product product = productRepo.findById(id).orElse(null);
        if (product != null) {
            buildImageUrl(product);
        }
        return product;
    }

    @Transactional(readOnly = true)
    public List<Product> getFeaturedProducts() {
        Specification<Product> spec = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("featured"), true);
        List<Product> products = productRepo.findAll(spec);
        products.forEach(this::buildImageUrl);
        return products;
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String categoryName) {
        List<Product> products = productRepo.findProductByCategory_Name(categoryName);
        if (products == null || products.isEmpty()) {
            return new ArrayList<>();
        }
        products.forEach(this::buildImageUrl);
        return products;
    }

    /**
     * Builds the full image URL for the product.
     * Uses readOnly transaction to prevent dirty-checking from persisting the URL change.
     */
    private void buildImageUrl(Product product) {
        String imageName = product.getImageURL();
        if (imageName != null && !imageName.isEmpty() && !imageName.startsWith("http")) {
            product.setImageURL("http://localhost:" + serverPort + contextPath + "/images/products/" + imageName);
        } else if (imageName == null || imageName.isEmpty()) {
            product.setImageURL("http://localhost:" + serverPort + contextPath + "/images/products/default-product.jpg");
        }
    }

    public Product addProduct(Product product) {
        return productRepo.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        Product updatesProduct = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        updatesProduct.setName(product.getName());
        updatesProduct.setDescription(product.getDescription());
        updatesProduct.setPrice(product.getPrice());
        updatesProduct.setCategory(product.getCategory());
        return productRepo.save(updatesProduct);
    }

    public Product updateStock(Long id, Integer stock) {
        Product updatesProduct = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        updatesProduct.setStock(stock);
        return productRepo.save(updatesProduct);
    }

    public Product updateFeaturedStatus(Long id, Boolean featured) {
        Product updatesProduct = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        updatesProduct.setFeatured(featured);
        return productRepo.save(updatesProduct);
    }

    public Product updateActiveStatus(Long id, Boolean active) {
        Product updatesProduct = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        updatesProduct.setActive(active);
        return productRepo.save(updatesProduct);
    }

    public void deleteById(Long id) {
        productRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Product> searchByKeyword(String keyword) {
        return productRepo.searchProducts(keyword);
    }

    public Product addReview(Long productId, Review review) {
        Product product = productRepo.findById(productId).
                orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        review.setProduct(product);
        product.getReviews().add(review);
        return productRepo.save(product);
    }

    @Transactional(readOnly = true)
    public List<Review> getProductReviews(Long productId) {
        Product product = productRepo.findById(productId).
                orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return product.getReviews();
    }
}
