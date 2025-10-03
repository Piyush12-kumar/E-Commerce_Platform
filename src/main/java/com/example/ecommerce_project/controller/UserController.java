package com.example.ecommerce_project.controller;

import com.example.ecommerce_project.DTO.LoginRequest;
import com.example.ecommerce_project.DTO.UserRegistrationDto;
import com.example.ecommerce_project.model.Address;
import com.example.ecommerce_project.model.Order;
import com.example.ecommerce_project.service.JwtService;
import com.example.ecommerce_project.service.TokenBlacklistService;
import com.example.ecommerce_project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.ecommerce_project.model.User;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        if (userService.existsByUsername(registrationDto.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        if (userService.existsByEmail(registrationDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use!");
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(registrationDto.getPassword());
        user.setPhoneNumber(registrationDto.getPhoneNumber());

        Set<String> roles = new HashSet<>();
        roles.add("USER");
        user.setRoles(roles);

        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping(value="/login", consumes = {"application/json", "application/json;charset=UTF-8"})
    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            if (authentication.isAuthenticated()) {
                // Fetch the full user object to get roles
                User user = userService.getUserByUsername(loginRequest.getUsername());
                // Pass the full user object to generate the token with roles
                return new ResponseEntity<>(jwtService.generateToken(user), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Authentication failed: " + e.getMessage(),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getProfile() {
        return new ResponseEntity<>(userService.getCurrentUser(), HttpStatus.OK);
    }

    @GetMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // Extract token expiration date
            Date expiryDate = jwtService.extractAllClaims(token).getExpiration();
            // Blacklist the token
            tokenBlacklistService.blacklistToken(token, expiryDate);
            // Clear the security context
            SecurityContextHolder.clearContext();
            return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("No authorization token found", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/deactivate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deactivateUser() {
        User currentUser = userService.getCurrentUser();
        userService.deactivateUser(currentUser.getUserId());
        return new ResponseEntity<>("User Account Deactivated successfully", HttpStatus.OK);
    }

    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> passwordData) {
        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");

        boolean isChanged = userService.changePassword(oldPassword, newPassword);
        if (isChanged) {
            return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/addresses")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Address>> getUserAddresses() {
        List<Address> addresses = userService.getUserAddresses();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @PostMapping("/addresses/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Address> addAddress(@RequestBody Address address) {
        Address savedAddress = userService.addAddress(address);
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }

    @DeleteMapping("/addresses/delete/{addressId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        userService.deleteAddress(addressId);
        return new ResponseEntity<>("Address deleted successfully", HttpStatus.OK);
    }

    @GetMapping("orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Order>> getUserOrders() {
        List<Order> orders = userService.getUserOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Order> getOrderDetails(@PathVariable Long orderId) {
        Order order = userService.getOrderDetails(orderId);
        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/promote-to-admin")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> promoteToAdmin(@RequestBody Map<String, String> request) {
        String secretToken = request.get("secretToken");
        String predefinedToken = jwtService.getAdminToken();

        if (!predefinedToken.equals(secretToken)) {
            return new ResponseEntity<>("Invalid token. Promotion to ADMIN denied.", HttpStatus.FORBIDDEN);
        }

        User currentUser = userService.getCurrentUser();
        Set<String> updatedRoles = new HashSet<>(currentUser.getRoles());
        updatedRoles.add("ADMIN");

        userService.updateUserRoles(currentUser, updatedRoles);

        return new ResponseEntity<>("User promoted to ADMIN successfully.", HttpStatus.OK);
    }

}