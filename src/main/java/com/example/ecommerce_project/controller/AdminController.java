// ecommerce_project/controller/AdminController.java
package com.example.ecommerce_project.controller;

import com.example.ecommerce_project.model.User;
import com.example.ecommerce_project.service.JwtService;
import com.example.ecommerce_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated() "+ "and hasRole('ADMIN')")
    public ResponseEntity<User> getProfile() {
        return new ResponseEntity<>(userService.getCurrentUser(), HttpStatus.OK);
    }

    @GetMapping("/logout")
    @PreAuthorize("isAuthenticated() "+ "and hasRole('ADMIN')")
    public ResponseEntity<String> logoutUser() {
        return new ResponseEntity<>("Admin logged out successfully", HttpStatus.OK);
    }

    @PutMapping("/update")
    @PreAuthorize("isAuthenticated() "+ "and hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/users/{userId}/deactivate")
    @PreAuthorize("isAuthenticated() "+ "and hasRole('ADMIN')")
    public ResponseEntity<String> deactivateUserAccount(@PathVariable Long userId) {
        userService.deactivateUser(userId);
        return new ResponseEntity<>("User account deactivated successfully", HttpStatus.OK);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUserStatus(@PathVariable Long id,
                                                   @RequestBody Map<String, Boolean> status) {
        userService.updateUserStatus(id, status.get("active"));
        return ResponseEntity.ok("User status updated successfully");
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }


}