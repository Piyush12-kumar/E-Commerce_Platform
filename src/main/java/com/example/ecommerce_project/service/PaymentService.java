package com.example.ecommerce_project.service;

import com.example.ecommerce_project.Dao.PaymentRepo;
import com.example.ecommerce_project.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class PaymentService {
    @Autowired
    PaymentRepo paymentRepo;
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public Payment processPayment(Payment payment) {
        // Validate payment
        if (payment == null || payment.getAmount() == null || payment.getAmount().doubleValue() <= 0) {
            logger.error("Invalid payment details");
            throw new IllegalArgumentException("Invalid payment details");
        }

        // Set additional fields
        payment.setStatus("PROCESSED");
        payment.setPaymentDate(LocalDateTime.now());
        payment.setRefunded(false);

        logger.info("Processing payment of {} via {}", payment.getAmount(), payment.getPaymentMethod());

        // Save and return payment
        return paymentRepo.save(payment);
    }

    public Payment getPaymentById(Long paymentId) {
        return paymentRepo.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id: " + paymentId));
    }

    public Payment refundPayment(Long paymentId) {
        Payment payment = getPaymentById(paymentId);
        if (payment.isRefunded()) {
            logger.warn("Payment with id {} has already been refunded", paymentId);
            return payment;
        }
        payment.setRefunded(true);
        payment.setRefundedAt(LocalDateTime.now());
        payment.setStatus("REFUNDED"); // Also update the status for clarity
        return paymentRepo.save(payment);
    }
}