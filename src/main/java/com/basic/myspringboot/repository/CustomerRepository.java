package com.basic.myspringboot.repository;

import com.basic.myspringboot.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Override
    Optional<Customer> findById(Long aLong);

    // Optional -> null 방지
    Optional<Customer> findByCustomerId(String customerId);

    List<Customer> findByCustomerNameContaining(String customerName);
}