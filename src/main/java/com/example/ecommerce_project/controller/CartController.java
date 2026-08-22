package com.example.ecommerce_project.controller;

import com.example.ecommerce_project.model.Cart;
import com.example.ecommerce_project.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cart")
@PreAuthorize("isAuthenticated()")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/items")
    public ResponseEntity<Cart> getCartItems() {
        return new ResponseEntity<>(cartService.getCartItems(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestParam Long productId) {
        Cart updatedCart = cartService.addToCart(productId);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Cart> removeFromCart(@PathVariable Long productId) {
        Cart updatedCart = cartService.removeFromCart(productId);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    @DeleteMapping("/remove-item/{productId}")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long productId) {
        Cart updatedCart = cartService.removeItemFromCart(productId);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Cart> clearCart() {
        Cart clearCart = cartService.clearCart();
        return new ResponseEntity<>(clearCart, HttpStatus.OK);
    }

}
