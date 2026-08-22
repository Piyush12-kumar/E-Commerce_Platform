package com.example.ecommerce_project.controller;

import com.example.ecommerce_project.model.Payment;
import com.example.ecommerce_project.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@PreAuthorize("isAuthenticated()")
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(@RequestBody Payment payment) {
        try {
            Payment processedPayment = paymentService.processPayment(payment);
            return ResponseEntity.ok(processedPayment);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/refund/{paymentId}")
    public ResponseEntity<Payment> refundPayment(@PathVariable Long paymentId) {
        try {
            Payment refundedPayment = paymentService.refundPayment(paymentId);
            return ResponseEntity.ok(refundedPayment);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
