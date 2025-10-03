package com.example.ecommerce_project.Dao;

import com.example.ecommerce_project.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {
}
