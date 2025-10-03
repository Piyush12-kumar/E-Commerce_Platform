package com.example.ecommerce_project.service;

import com.example.ecommerce_project.Dao.AddressRepo;
import com.example.ecommerce_project.Dao.UserRepo;
import com.example.ecommerce_project.exception.ResourceNotFoundException;
import com.example.ecommerce_project.exception.UnauthorizedException;
import com.example.ecommerce_project.model.Address;
import com.example.ecommerce_project.model.Order;
import com.example.ecommerce_project.model.User;
import com.example.ecommerce_project.model.UserPrincipal;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // Import the interface
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService { // Make this class implement the interface

    @Autowired
    private UserRepo repo;
    @Autowired
    private AddressRepo addressRepo;

    // This is now the ONLY implementation and it's the correct one.
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        // The UserPrincipal class correctly wraps your User entity for Spring Security.
        return new UserPrincipal(user);
    }

    @Transactional
    public User saveUser(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(true);
        return repo.save(user);
    }

    @Transactional
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UnauthorizedException("User is not authenticated");
        }
        String username = authentication.getName();
        User user = repo.findByUsername(username);
        if (user == null)
            throw new ResourceNotFoundException("User not found with username: " + username);
        return user;
    }

    // Helper method used by the login controller
    public User getUserByUsername(String username) {
        User user = repo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    // --- Other methods in your service remain the same ---

    @Transactional
    public User updateUser(User user) {
        User currentUser = getCurrentUser();
        if (user.getUsername() != null) currentUser.setUsername(user.getUsername());
        if (user.getEmail() != null) currentUser.setEmail(user.getEmail());
        if (user.getPhoneNumber() != null) currentUser.setPhoneNumber(user.getPhoneNumber());
        return repo.save(currentUser);
    }
    @Transactional
    public void deactivateUser(Long userId) {
        User user = repo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setEnabled(false);
        repo.save(user);
    }
    @Transactional
    public boolean changePassword(String oldPassword, String newPassword) {
        User user = getCurrentUser();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(encoder.encode(newPassword));
            repo.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public void updateUserRoles(User user, Set<String> newRoles) {
        user.setRoles(newRoles);
        repo.save(user);
    }

    // All other existing methods in your UserService.java file...
    public List<User> getAllUsers() { return repo.findAll(); }
    public User getUserById(Long id) { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)); }
    @Transactional
    public void updateUserStatus(Long id, boolean active) {
        User user = getUserById(id);
        user.setEnabled(active);
        repo.save(user);
    }
    @Transactional
    public void deleteUser(Long id) { repo.deleteById(id); }
    public boolean existsByUsername(String username) { return repo.existsUsersByUsername(username); }
    public boolean existsByEmail(String email) { return repo.existsUsersByEmail(email); }
    public List<Order> getUserOrders() { return getCurrentUser().getOrders().stream().toList(); }
    public Order getOrderDetails(Long orderId) {
        return getCurrentUser().getOrders().stream()
                .filter(order -> order.getOrderId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }
    @Transactional
    public List<Address> getUserAddresses() {
        return getCurrentUser().getAddresses().stream().toList();
    }
    @Transactional
    public Address addAddress(Address newAddress) {
        User user = getCurrentUser();
        newAddress.setUser(user);
        if (newAddress.isDefault() && user.getAddresses() != null) {
            user.getAddresses().forEach(addr -> addr.setDefault(false));
        } else if (user.getAddresses() == null || user.getAddresses().isEmpty()) {
            newAddress.setDefault(true);
        }
        return addressRepo.save(newAddress);
    }
    @Transactional
    public void deleteAddress(Long addressId) {
        User user = getCurrentUser();
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));
        if (!address.getUser().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedException("Address does not belong to the current user.");
        }
        addressRepo.delete(address);
    }
}