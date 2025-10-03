package com.example.ecommerce_project.service;

import com.example.ecommerce_project.Dao.DiscountRepo;
import com.example.ecommerce_project.model.Discount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DiscountService {
    @Autowired
    DiscountRepo discountRepository;
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public Discount addDiscount(Discount discount) {
        if (discount.getId() != null && discountRepository.existsById(discount.getId())) {
            throw new IllegalArgumentException("Discount with this ID already exists.");
        }
        return discountRepository.save(discount);
    }

    public Discount updateDiscount(Long id, Discount discount) {
        if (!discountRepository.existsById(id)) {
            throw new IllegalArgumentException("Discount with this ID does not exist.");
        }
        discount.setId(id);
        return discountRepository.save(discount);
    }

    public void deleteDiscount(Long id) {
        if (!discountRepository.existsById(id)) {
            throw new IllegalArgumentException("Discount with this ID does not exist.");
        }
        discountRepository.deleteById(id);
    }

    public Discount getDiscountById(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found with id: " + id));
    }
}
