package com.example.ecommerce_project.DTO;

public class ReviewDto {
    private Long productId;
    private int rating;
    private String comment;

    public ReviewDto() {
    }
    public ReviewDto(Long productId, int rating, String comment) {
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
    }
    // Getters and Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
