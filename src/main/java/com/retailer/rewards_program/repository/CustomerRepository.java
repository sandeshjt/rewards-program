package com.retailer.rewards_program.repository;

import com.retailer.rewards_program.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,String> {

    @Override
    List<Customer> findAll();

    Optional<Customer> findByCustomerId(String customerId);
}
