package com.example.ecommerce_project.service;

import com.example.ecommerce_project.Dao.CartItemRepo;
import com.example.ecommerce_project.Dao.CartRepo;
import com.example.ecommerce_project.Dao.ProductRepo;
import com.example.ecommerce_project.exception.ResourceNotFoundException;
import com.example.ecommerce_project.model.Cart;
import com.example.ecommerce_project.model.CartItem;
import com.example.ecommerce_project.model.Product;
import com.example.ecommerce_project.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    CartRepo cartRepo;
    @Autowired
    UserService userService;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    private CartItemRepo cartItemRepo;

    public Cart getCartItems() {
        User user = userService.getCurrentUser();
        return cartRepo.findByUser(user);
    }

    @Transactional
    public Cart addToCart(Long productId) {
        User user = userService.getCurrentUser();
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Get the cart. If it doesn't exist, create and save it immediately.
        Cart cart = cartRepo.findByUser(user);
        if (cart == null) {
            cart = new Cart(user);
            // **FIXED**: Save the new cart as soon as it's created to get an ID.
            cartRepo.save(cart);
        }

        // Check if the item already exists in the cart
        final Cart finalCart = cart; // Final variable for use in lambda
        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // If the item exists, just increase the quantity.
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            // If the item does not exist, create a new one and add it to the cart.
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product);
            newCartItem.setQuantity(1);
            newCartItem.setPrice(product.getPrice());
            newCartItem.setCart(finalCart); // Link the item to the persistent cart
            cart.getItems().add(newCartItem);
        }

        // Save the cart. CascadeType.ALL will automatically save the new or updated CartItem.
        return cartRepo.save(cart);
    }

    @Transactional
    public Cart removeFromCart(Long productId) {
        Cart cart = getCartItems();
        if (cart == null) {
            throw new ResourceNotFoundException("Cart not found for the user");
        }

        // Find and remove the CartItem.
        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart with id: " + productId));

        cart.getItems().remove(itemToRemove);
        cartItemRepo.delete(itemToRemove); // Explicitly delete the orphaned item

        return cartRepo.save(cart);
    }

    @Transactional
    public Cart clearCart() {
        Cart cart = getCartItems();
        if (cart != null && !cart.getItems().isEmpty()) {
            // Thanks to orphanRemoval=true, clearing the list will delete the items.
            cart.getItems().clear();
            return cartRepo.save(cart);
        }
        return cart;
    }
}